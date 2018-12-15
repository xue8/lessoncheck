package term.rjb.x2l.lessoncheck.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Sign;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Student;
import term.rjb.x2l.lessoncheck.presenter.TeacherPresenter;

//签到记录类
class CheckMessage{
    public String time;
    public String isDone;
    public String id;
    public CheckMessage(String time,String id,Integer i)
    {
        this.time=time;
        isDone=(i==0)?"进行中":"已结束";
        this.id=id;
    }
    public CheckMessage(String time,Integer i)
    {
        this.time=time;
        isDone=(i==1)?"到课":"缺勤";
    }
}

//学生记录类
class StudentMessage{
    public String number;
    public String name;
    public StudentMessage(String number,String name)
    {
        this.number=number;
        this.name=name;
    }
}

public class TeacherClassActivity extends AppCompatActivity implements term.rjb.x2l.lessoncheck.Utils.View {
    private  String classNum;
    private Toolbar toolBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View checkView, studentsView;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private List<CheckMessage> checkMessageList = new ArrayList<>();
    private List<StudentMessage> studentMessageList = new ArrayList<>();
    private  ListView checkListView;
    private  ListView studentsListView;
    private TeacherPresenter teacherPresenter;
    private Handler handler = new Handler(){
      public void handleMessage(Message message){
          switch (message.what){
              case 0:
                  checkMessageList.clear();
                  List<Lesson_Sign> lesson_signs = (List<Lesson_Sign>) message.obj;
                  if(lesson_signs.size()>0) {
                      for (Lesson_Sign lesson_sign : lesson_signs) {
                          SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                          Date nowDate = new Date();
                          Calendar rightNow = Calendar.getInstance();
                          Date createdAt = new Date();
                          try{
                              createdAt = df.parse(lesson_sign.getCreatedAt());
                          }catch (ParseException e1){
                              e1.printStackTrace();
                          }
                          rightNow.setTime(createdAt);
                          rightNow.add(Calendar.MINUTE, lesson_sign.getLastMinute());
                          if( nowDate.before(rightNow.getTime()) ) {
                              checkMessageList.add(new CheckMessage(lesson_sign.getCreatedAt(), lesson_sign.getSignNumber(),0 ));
                          }else{
                              checkMessageList.add(new CheckMessage(lesson_sign.getCreatedAt(), lesson_sign.getSignNumber(),1 ));
                          }
                          Collections.reverse(checkMessageList);
                      }
                  }
                  initCheckMessage();
                  break;
              case 1:
                  studentMessageList.clear();
                  List<Lesson_Student> lesson_students = (List<Lesson_Student>)message.obj;
                  if(lesson_students.size()>0){
                      for(Lesson_Student lesson_student : lesson_students){
                         studentMessageList.add(new StudentMessage(lesson_student.getStudent().getNumber(),lesson_student.getStudent().getName()));
                         Log.d("测试","学号和姓名"+lesson_student.getStudent().getNumber()+lesson_student.getStudent().getName()+lesson_student.getLessonNumber());
                      }
                  }
                  initStudentMessage();
                  break;
          }
      }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class);
        ActivityManager.getAppManager().addActivity(this);
        Intent intent=getIntent();
        classNum=intent.getStringExtra("classNumber");
        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("课堂详情");
        toolBar.setSubtitle(classNum);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCheckMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_teacher_class, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action1:
                Intent intent=new Intent(TeacherClassActivity.this,CreateCheckActivity.class);
                intent.putExtra("classNumber",classNum);
                startActivity(intent);
               // Toast.makeText(TeacherClassActivity.this, "进入发布签到页面", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                ActivityManager.getAppManager().finishActivity(TeacherClassActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init()
    {
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout =  findViewById(R.id.layout_tab);
        mInflater = LayoutInflater.from(this);
        checkView = mInflater.inflate(R.layout.pager_check_message, null);
        studentsView = mInflater.inflate(R.layout.pager_students_message, null);
        checkListView=checkView.findViewById(R.id.list_view);
        studentsListView=studentsView.findViewById(R.id.list_view);
        mViewList.add(checkView);
        mViewList.add(studentsView);
        teacherPresenter = new TeacherPresenter(this);
        //获取签到记录
        mTitleList.add("课堂签到记录");
        mTitleList.add("我的学生");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)),true);//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)  {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position )
                {
                    case  0:
                        initCheckMessage();
                        break;
                    case  1:
                        initStudentMessage();
                        break;
                }
            }
            @Override
         public void onPageScrollStateChanged(int state) {

    }
    });
        initCheckMessage();
    }

    @Override
    public void getALLessons(List<Lesson> lessons) {

    }

    //适配器借鉴简书
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }
    }

    void initCheckMessage()
    {
        //TODO 后端->查询数据库里的这个课堂的签到记录数组 包括时间 是否结束（该课堂发布签到了几次）

        //用到的数据：
        //CheckMessage 签到记录类
        //classNum 课堂代码

        //begin
        //样例 添加一个新的记录对象到前端写的链表checkMessageList中
         //checkMessageList.add(new CheckMessage("2018-12-11",0)); 注意 1代表进行中,0代表 结束
        //end
        teacherPresenter.getSignByLesson(classNum,handler);
        final ArrayAdapter<CheckMessage> adapter1 = new ArrayAdapter<CheckMessage>(TeacherClassActivity.this,
                R.layout.class_check_message,checkMessageList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CheckMessage checkMessage =  getItem(position);
                LayoutInflater layoutInflater = getLayoutInflater();

                View view = layoutInflater.inflate(R.layout.class_check_message, parent, false);

                TextView CheckMessageTime = view.findViewById(R.id.tv_check_time);
                TextView CheckMessageStatus =  view.findViewById(R.id.tv_check_ok);

                CheckMessageTime.setText(checkMessage.time);
                CheckMessageStatus.setText(checkMessage.isDone);
                return view;
            }
        };
        checkListView.setAdapter(adapter1);
        checkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              //  Toast.makeText(TeacherClassActivity.this,"你选择了" + checkMessageList.get(i).id+"的签到记录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(TeacherClassActivity.this,CheckAllStudentActivity.class);
                intent.putExtra("classNum",classNum);
                intent.putExtra("checkID",checkMessageList.get(i).id);
                intent.putExtra("time",checkMessageList.get(i).time);
                intent.putExtra("erweima",checkMessageList.get(i).isDone);
                startActivity(intent);
            }
        });
    }
    void initStudentMessage()
    {
        //TODO 后端->查询数据库里选了这个课的学生 包括学生用户名 名字

        //用到的数据
        //StudentMessage 学生数据类
        //classNum 课堂代码

        //begin
        //样例 新建一个学生数据对象加入到前端的链表studentMessageList中
        //studentMessageList.add(new StudentMessage("1600300924","luqi"));
        //end

        teacherPresenter.getAllLessonStudents(classNum,handler);
        final ArrayAdapter<StudentMessage> adapter2 = new ArrayAdapter<StudentMessage>(TeacherClassActivity.this,
                R.layout.class_student_message,studentMessageList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                StudentMessage studentMessage =  getItem(position);
                LayoutInflater layoutInflater = getLayoutInflater();

                View view = layoutInflater.inflate(R.layout.class_student_message, parent, false);

                TextView StudentMessageNumber = view.findViewById(R.id.tv_class_student_number);
                TextView StudentMessageName = view.findViewById(R.id.tv_class_student_name);
                StudentMessageNumber.setText(studentMessage.number);
                StudentMessageName.setText(studentMessage.name);
                return view;
            }
        };
        studentsListView.setAdapter(adapter2);
        studentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

              //  Toast.makeText(TeacherClassActivity.this,"你选择了" + studentMessageList.get(i).number+"的签到信息",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(TeacherClassActivity.this,UniversalStudentCheckActivity.class);
                intent.putExtra("classNum",classNum);
                intent.putExtra("studentNum",studentMessageList.get(i).number);
                intent.putExtra("isTeacher",1);
                startActivity(intent);
            }
        });
    }
}

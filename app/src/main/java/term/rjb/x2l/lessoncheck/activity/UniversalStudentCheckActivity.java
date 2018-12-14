package term.rjb.x2l.lessoncheck.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.Sign_Student;
import term.rjb.x2l.lessoncheck.presenter.StudentPresenter;
import term.rjb.x2l.lessoncheck.presenter.TeacherPresenter;
import term.rjb.x2l.lessoncheck.zxing.activity.CaptureActivity;


/**
 *  通用窗口 查看学生的某课堂的所有签到记录 学生看自己 教师看指定学生
 */
public class UniversalStudentCheckActivity extends AppCompatActivity implements term.rjb.x2l.lessoncheck.Utils.View {
    private Toolbar toolBar;
    private Integer who;
    private ListView listView;
    private List<CheckMessage> studentMessageList= new ArrayList<>();
    private TeacherPresenter teacherPresenter;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;
    private ArrayAdapter<CheckMessage> adapter1;

    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 6 :
                    List<Sign_Student> sign_students = (List<Sign_Student>) message.obj;
                    studentMessageList.clear();
                    for (int i = 0; i <sign_students.size(); i++){
                        System.out.println("---------" + sign_students.get(i).getIsSign());
                        studentMessageList.add(new CheckMessage( sign_students.get(i).getCreatedAt(), sign_students.get(i).getIsSign()));
                    }
                    Collections.reverse(studentMessageList);
//                    if(sign_students.size()>0){
//                        for(Sign_Student sign_student : sign_students){
//                            studentMessageList.add(new CheckMessage(sign_student.getCreatedAt().substring(0, sign_student.getCreatedAt().indexOf(" ")),sign_student.getIsSign()));//1到课 0缺勤
//                        }
//                    }
                    adapter1 = new ArrayAdapter<CheckMessage>(UniversalStudentCheckActivity.this,
                            R.layout.class_check_message,studentMessageList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            CheckMessage checkMessage =  getItem(position);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            View view = layoutInflater.inflate(R.layout.class_check_message, parent, false);

                            TextView CheckMessageTime = view.findViewById(R.id.tv_check_time);
                            TextView CheckMessageStatus =  view.findViewById(R.id.tv_check_ok);

                            CheckMessageTime.setText(checkMessage.time);
                            CheckMessageStatus.setText(checkMessage.isDone);
                            if(checkMessage.isDone.equals("缺勤"))
                            {
                                CheckMessageStatus.setTextColor(getResources().getColor(R.color.red));
                            }
                            else
                            {
                                CheckMessageStatus.setTextColor(getResources().getColor(R.color.green));
                            }
                            return view;
                        }
                    };
                    listView.setAdapter(adapter1);
                case 20:
                    studentMessageList.clear();
                    List<Sign_Student> list_l = (List<Sign_Student>)message.obj;
                    studentMessageList.clear();
                    for (int i = list_l.size()-1; i >= 0; i--){
                        //TODO 后端->根据课号获取这个课号的所有签到记录ID 然后用它搜索这个学生在这个课堂的所有签到记录 返回一个CheckMessage数组
                        //用到的数据
                        //Student  学生用户名
                        //classNum 课堂号码

                        //样例    时间 是否签到
//                        studentMessageList.add(new CheckMessage("2018-10-3",0));//1到课 0缺勤
//                        studentMessageList.add(new CheckMessage("2018-10-2",1));//1到课 0缺勤
//                        studentMessageList.add(new CheckMessage("2018-10-1",0));//1到课 0缺勤
//                        studentMessageList.add(new CheckMessage("2018-9-27",1));//1到课 0缺勤
                        //
                        System.out.println("---------" + list_l.get(i).getIsSign());
                        studentMessageList.add(new CheckMessage(list_l.get(i).getCreatedAt(),list_l.get(i).getIsSign()));//1到课 0缺勤
                    }
                    adapter1 = new ArrayAdapter<CheckMessage>(UniversalStudentCheckActivity.this,
                            R.layout.class_check_message,studentMessageList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            CheckMessage checkMessage =  getItem(position);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            View view = layoutInflater.inflate(R.layout.class_check_message, parent, false);

                            TextView CheckMessageTime = view.findViewById(R.id.tv_check_time);
                            TextView CheckMessageStatus =  view.findViewById(R.id.tv_check_ok);

                            CheckMessageTime.setText(checkMessage.time);
                            CheckMessageStatus.setText(checkMessage.isDone);
                            if(checkMessage.isDone.equals("缺勤"))
                            {
                                CheckMessageStatus.setTextColor(getResources().getColor(R.color.red));
                            }
                            else
                            {
                                CheckMessageStatus.setTextColor(getResources().getColor(R.color.green));
                            }
                            return view;
                        }
                    };
                    listView.setAdapter(adapter1);
//                    ActivityManager.getAppManager().finishActivity(UniversalStudentCheckActivity.this);
                    break;
                case 17:
                    Toast.makeText(UniversalStudentCheckActivity.this,"签到成功！",Toast.LENGTH_SHORT).show();
                    initList();
                    ActivityManager.getAppManager().finishActivity(UniversalStudentCheckActivity.this);
                    break;
                case 16:
                    Toast.makeText(UniversalStudentCheckActivity.this,"签到失败！",Toast.LENGTH_SHORT).show();
                    break;
                case 18:
                    Toast.makeText(UniversalStudentCheckActivity.this,"签到失败，口令出错！",Toast.LENGTH_SHORT).show();
                    break;
                case 15:
                    Toast.makeText(UniversalStudentCheckActivity.this,"签到失败，签到时间已过！",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    } ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_student_check);
        ActivityManager.getAppManager().addActivity(this);
        toolBar = this.findViewById(R.id.toolbar);
        Intent intent=getIntent();
        who=intent.getIntExtra("isTeacher",0);
        listView=findViewById(R.id.list_view);
        initToolBar();
        //测试
        initList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(who==1)
        return super.onCreateOptionsMenu(menu);
        else
        {
            MenuInflater inflater = this.getMenuInflater();
            inflater.inflate(R.menu.menu_student_toolbar, menu);
            return true;
        }
    }

    //顶部后退按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId() )
        {
            case  android.R.id.home:
                ActivityManager.getAppManager().finishActivity(this);
                break;
            case R.id.action1:
                Intent intent=new Intent(UniversalStudentCheckActivity.this,StudentCheckActivity.class);

                String classNum= getIntent().getStringExtra("classNum");
                intent.putExtra("classNum", classNum);

                startActivity(intent);
                break;
            case R.id.action2:

                // 扫二维码
                if (ContextCompat.checkSelfPermission(UniversalStudentCheckActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UniversalStudentCheckActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    Intent intent1 = new Intent(UniversalStudentCheckActivity.this, CaptureActivity.class);

                    String classNum1= getIntent().getStringExtra("classNum");
                    intent1.putExtra("classNum", classNum1);

                    startActivity(intent1);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

      public   void initList()
    {
        Toast.makeText(UniversalStudentCheckActivity.this,"正在拉取信息",Toast.LENGTH_SHORT).show();
            String classNum= getIntent().getStringExtra("classNum");
            String studentNum= getIntent().getStringExtra("studentNum");
            //TODO 后端->根据课号获取这个课号的所有签到记录ID 然后用它搜索这个学生在这个课堂的所有签到记录 返回一个CheckMessage数组
            //用到的数据
            //Student  学生用户名
            //classNum 课堂号码

            if (who==1)
            {
                teacherPresenter  = new TeacherPresenter(this);
                teacherPresenter.getAllSignMessageByStudentNumber(classNum,studentNum,handler);
            }
            else
            {
                System.out.println( "classNum"+ classNum);
                StudentPresenter studentPresenter = new StudentPresenter();
                studentPresenter.getSignStatusByLessonId(classNum,handler);
            }

            //样例    时间 是否签到
        
            //


//            final ArrayAdapter<CheckMessage> adapter1 = new ArrayAdapter<CheckMessage>(UniversalStudentCheckActivity.this,
//                    R.layout.class_check_message,studentMessageList){
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    CheckMessage checkMessage =  getItem(position);
//                    LayoutInflater layoutInflater = getLayoutInflater();
//                    View view = layoutInflater.inflate(R.layout.class_check_message, parent, false);
//
//                    TextView CheckMessageTime = view.findViewById(R.id.tv_check_time);
//                    TextView CheckMessageStatus =  view.findViewById(R.id.tv_check_ok);
//
//                    CheckMessageTime.setText(checkMessage.time);
//                    CheckMessageStatus.setText(checkMessage.isDone);
//                    if(checkMessage.isDone.equals("缺勤"))
//                    {
//                        CheckMessageStatus.setTextColor(getResources().getColor(R.color.red));
//                    }
//                    else
//                    {
//                        CheckMessageStatus.setTextColor(getResources().getColor(R.color.green));
//                    }
//                    return view;
//                }
//            };
//            listView.setAdapter(adapter1);
    }
    void  initToolBar()
    {
        //是否是老师来初始化标题和副标题
        if (who==1)
        {
            String time="";
            String Student= getIntent().getStringExtra("studentNum");
            toolBar.setTitle(Student);
            toolBar.setSubtitle(time);
        }
        else
        {
            String classNum= getIntent().getStringExtra("classNum");
            toolBar.setTitle("我的签到详情");
            toolBar.setSubtitle(classNum);
        }
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void getALLessons(List<Lesson> lessons) {

    }
}

package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;


/**
 *  通用窗口 查看学生的某课堂的所有签到记录 学生看自己 教师看指定学生
 */
public class UniversalStudentCheckActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private Integer who;
    private ListView listView;
    private List<CheckMessage> studentMessageList= new ArrayList<>();
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

    //顶部后退按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityManager.getAppManager().finishActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    void initList()
    {

            String classNum= getIntent().getStringExtra("classNum");
            String Student= getIntent().getStringExtra("studentNum");
            //TODO 后端->根据课号获取这个课号的所有签到记录ID 然后用它搜索这个学生在这个课堂的所有签到记录 返回一个CheckMessage数组
            //用到的数据
            //Student  学生用户名
            //classNum 课堂号码

            //样例    时间 是否签到
            studentMessageList.add(new CheckMessage("2018-10-3",0));//1到课 0缺勤
            studentMessageList.add(new CheckMessage("2018-10-2",1));//1到课 0缺勤
            studentMessageList.add(new CheckMessage("2018-10-1",0));//1到课 0缺勤
            studentMessageList.add(new CheckMessage("2018-9-27",1));//1到课 0缺勤
            //


            final ArrayAdapter<CheckMessage> adapter1 = new ArrayAdapter<CheckMessage>(UniversalStudentCheckActivity.this,
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
}

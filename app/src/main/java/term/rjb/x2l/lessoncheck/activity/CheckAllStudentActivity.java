package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.Sign_Student;
import term.rjb.x2l.lessoncheck.presenter.TeacherPresenter;

class Message{
    public StudentMessage student;
    public boolean isChecked;
    public Message(StudentMessage student, boolean target)
    {
        this.student=student;
        this.isChecked=target;
    }
}

public class CheckAllStudentActivity extends AppCompatActivity implements term.rjb.x2l.lessoncheck.Utils.View {
    private Toolbar toolBar;
    private ListView listView;
    private List<Message> messageList= new ArrayList<>();
    private TeacherPresenter teacherPresenter;
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message message) {
            switch (message.what){
                case 0:
                    List<Sign_Student> sign_students = (List<Sign_Student>)message.obj;
                    if(sign_students.size()>0){
                        for(Sign_Student sign_student : sign_students){
                            messageList.add(new Message(new StudentMessage(sign_student.getStudent().getNumber(),sign_student.getStudent().getName()),sign_student.getIsSign()==1));//true到课 false缺勤
                        }
                    }

                    final ArrayAdapter<Message> adapter1 = new ArrayAdapter<Message>(CheckAllStudentActivity.this,
                            R.layout.class_check_message,messageList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Message checkMessage =  getItem(position);
                            LayoutInflater layoutInflater = getLayoutInflater();
                            View view = layoutInflater.inflate(R.layout.class_check_message, parent, false);

                            TextView CheckMessageStudent = view.findViewById(R.id.tv_check_time);
                            TextView CheckMessageStatus =  view.findViewById(R.id.tv_check_ok);

                            CheckMessageStudent.setText(checkMessage.student.number+" "+checkMessage.student.name);
                            if(checkMessage.isChecked)
                            {
                                CheckMessageStatus.setText("到课");
                                CheckMessageStatus.setTextColor(getResources().getColor(R.color.green));
                            }
                            else
                            {
                                CheckMessageStatus.setText("缺勤");
                                CheckMessageStatus.setTextColor(getResources().getColor(R.color.red));
                            }
                            return view;
                        }
                    };
                    listView.setAdapter(adapter1);

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_all_student);
        ActivityManager.getAppManager().addActivity(this);
        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("签到详情");
        Intent intent=getIntent();
        listView=findViewById(R.id.list_view);
        String time=intent.getStringExtra("time");
        toolBar.setSubtitle(time);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean isDone=intent.getStringExtra("erweima").equals("进行中");
        initImage(isDone);
        initKey(isDone);
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

    //加载进行中的签到的二维码
    void initImage(boolean isDone)
    {
        if (!isDone)
            return;
        //TODO 后端->加载二维码
        String checkID= getIntent().getStringExtra("checkID");
        ImageView erweima=findViewById(R.id.image_erWeiMa);
        erweima.setImageURI(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + "/qr_code" + checkID + ".png")));
    }
    //加载口令，进行中则口令，否则已过时
    void initKey(boolean isDone)
    {

        TextView key=findViewById(R.id.tv_key);

        //样例
        if (isDone) {
            //TODO 后端->加载口令
            String checkID= getIntent().getStringExtra("checkID");
            key.setText(checkID);
        }
        else
            key.setText("已结束");
    }
    void initList()
    {

        String checkID= getIntent().getStringExtra("checkID");
        String classNum= getIntent().getStringExtra("classNum");
        //TODO 后端->根据签到记录ID 搜索这课堂下所有学生对于这次签到的记录 返回一个CheckMessage数组

        //用到的数据
        //Student  学生用户名
        //checkID 签到ID

        //样例    new学生(学号,名字) 是否签到
        teacherPresenter = new TeacherPresenter(this);
        teacherPresenter.getSignedStudent(checkID,handler);

        //

//
//        final ArrayAdapter<Message> adapter1 = new ArrayAdapter<Message>(CheckAllStudentActivity.this,
//                R.layout.class_check_message,messageList){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                Message checkMessage =  getItem(position);
//                LayoutInflater layoutInflater = getLayoutInflater();
//                View view = layoutInflater.inflate(R.layout.class_check_message, parent, false);
//
//                TextView CheckMessageStudent = view.findViewById(R.id.tv_check_time);
//                TextView CheckMessageStatus =  view.findViewById(R.id.tv_check_ok);
//
//                CheckMessageStudent.setText(checkMessage.student.number+" "+checkMessage.student.name);
//                if(checkMessage.isChecked)
//                {
//                    CheckMessageStatus.setText("到课");
//                    CheckMessageStatus.setTextColor(getResources().getColor(R.color.green));
//                }
//                else
//                {
//                    CheckMessageStatus.setText("缺勤");
//                    CheckMessageStatus.setTextColor(getResources().getColor(R.color.red));
//                }
//                return view;
//            }
//        };
//        listView.setAdapter(adapter1);
    }

    @Override
    public void getALLessons(List<Lesson> lessons) {

    }
}

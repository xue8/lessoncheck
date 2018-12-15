package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import cn.bmob.v3.BmobUser;
import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Student;
import term.rjb.x2l.lessoncheck.pojo.TheClass;
import term.rjb.x2l.lessoncheck.pojo.User;
import term.rjb.x2l.lessoncheck.presenter.StudentPresenter;
import term.rjb.x2l.lessoncheck.presenter.TeacherPresenter;

public class AddClassActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private  String username;
    private EditText classNumber;
    private Button addClass;
    private Lesson lesson;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent;
            String name="";
            switch (msg.what) {
                case 19:
                    //TODO 后端->把添加后课堂的objID 和课堂名 教师名 返回过来生成新的数据对象传入UI列表数组！
                    Lesson lesson = new Lesson();
                    lesson =(Lesson)msg.obj;
                    String teacherName=lesson.getTeacherName();
                    String className=lesson.getLessonName();
                    String classObjID=lesson.getObjectId();

                    TheClass temp=new TheClass(teacherName,className,classNumber.getText().toString().trim(),0,classObjID);
                    ((StudentMainActivity)ActivityManager.getAppManager().getActivity(StudentMainActivity.class)).addClass(temp);
                    Toast.makeText(AddClassActivity.this, "加入成功", Toast.LENGTH_SHORT).show();
                    ActivityManager.getAppManager().finishActivity(AddClassActivity.this);
                    break;
                case 18:
                    Toast.makeText(AddClassActivity.this, "不存在课号", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        ActivityManager.getAppManager().addActivity(this);

        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("加入课堂");
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        classNumber=findViewById(R.id.et_classNum);
        addClass=findViewById(R.id.btn_addClass);
        username=intent.getStringExtra("username");
        classNumber.setFilters(new InputFilter[]{inputFilter});

        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lesson = new Lesson();
                lesson.setLessonNumber(classNumber.getText().toString().trim());
                User user = BmobUser.getCurrentUser(User.class);
                lesson.setTeacher(user);

                StudentPresenter studentPresenter = new StudentPresenter();
                studentPresenter.joinClassByLessonId(classNumber.getText().toString().trim(),handler);
                //TODO 后端->加入课堂 记得时间 返回一个是否成功的信号
                //用得到数据有：
                //classNumber.getText().toString().trim() 想要加入的课堂代码
                //username 学生用户名
                //addSuccess是否加入成功

//                boolean addSuccess=false;
//                if (addSuccess)
//                {
//                    //TODO 后端->把添加后课堂的objID 和课堂名 教师名 返回过来生成新的数据对象传入UI列表数组！
//                    String teacherName="";
//                    String className="";
//                    String classObjID="";
//
//                    TheClass temp=new TheClass(teacherName,className,classNumber.getText().toString().trim(),0,classObjID);
//                    ((TeacherMainActivity)ActivityManager.getAppManager().getActivity(TeacherMainActivity.class)).addClass(temp);
//                    ActivityManager.getAppManager().finishActivity(AddClassActivity.this);
//                }
//                else
//                {
//                    Toast.makeText(AddClassActivity.this, "不存在课号", Toast.LENGTH_SHORT).show();
//                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityManager.getAppManager().finishActivity(AddClassActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    //输入框限制功能
    InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {

                int destLen = dest.toString().getBytes("GB18030").length;
                int sourceLen = source.toString().getBytes("GB18030").length;
                if (destLen + sourceLen > 7) {
                    Toast.makeText(AddClassActivity.this,"最多可以输入7个英文或数字",Toast.LENGTH_SHORT).show();
                    return "";
                }
                //如果按返回键
                if (source.length() < 1 && (dend - dstart >= 1)) {
                    return dest.subSequence(dstart, dend - 1);
                }
                return source;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
    };
}

package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.TheClass;
import term.rjb.x2l.lessoncheck.presenter.RegisterPresenter;

public class CreateClassActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private  String name;
    private EditText className;
    private EditText classNumber;
    private  Button createClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        ActivityManager.getAppManager().addActivity(this);

        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("添加课堂");
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        className=findViewById(R.id.et_className);
        classNumber=findViewById(R.id.et_classNum);
        createClass=findViewById(R.id.btn_createClass);
        name=intent.getStringExtra("name");
        classNumber.setFilters(new InputFilter[]{inputFilter});
        createClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 后端->添加课堂 记得时间 返回一个是否成功的信号

                //用道德数据有：
                //className.getText().toString().trim() 课堂名称
                //classNumber.getText().toString().trim() 课堂代码
                //name 教师名

                //如果添加成功 执行下面的代码
                TheClass temp=new TheClass(name,className.getText().toString().trim(),classNumber.getText().toString().trim(),0);
                ((TeacherMainActivity)ActivityManager.getAppManager().getActivity(TeacherMainActivity.class)).addClass(temp);
                ActivityManager.getAppManager().finishActivity(CreateClassActivity.this);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityManager.getAppManager().finishActivity(CreateClassActivity.this);
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
                if (destLen + sourceLen > 16) {
                    Toast.makeText(CreateClassActivity.this,"最多可以输入7个英文或数字",Toast.LENGTH_SHORT).show();
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

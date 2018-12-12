package term.rjb.x2l.lessoncheck.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Student;
import term.rjb.x2l.lessoncheck.pojo.User;
import term.rjb.x2l.lessoncheck.presenter.RegisterPresenter;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private EditText password;
    private EditText user;
    private EditText name;
    private EditText answers;
    private Spinner miBao;
    private Spinner sex;
   private SwitchCompat job;
    private Map<String,Integer> mibaoMap;
    RegisterPresenter registerPresenter;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityManager.getAppManager().addActivity(this);

        registerPresenter = new RegisterPresenter(this);
        miBao=findViewById(R.id.sp_mibao);
        sex=findViewById(R.id.sp_sex);
        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("注册");
        toolBar.setSubtitle("成为我们的一员");
        password=findViewById(R.id.et_password);
        register=findViewById(R.id.btn_registerAndLogin);
        register.setOnClickListener(onClickListener);
        name=findViewById(R.id.et_name);
        answers=findViewById(R.id.et_answers);
        user=findViewById(R.id.et_user);
        job=findViewById(R.id.sw_job);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        password.setFilters(new InputFilter[]{inputFilter});

        mibaoMap=new HashMap<>();
        mibaoMap.put("你的生日",0);
        mibaoMap.put("你毕业的高中名字",1);
        mibaoMap.put("你的小名",2);

    }
    //顶部后退按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityManager.getAppManager().finishActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_registerAndLogin:
                    registerAndLogin();
                    break;
            }
        }
    };

    //注册并登录
    void registerAndLogin()
    {
        if(user.getText().length()<10)
        {
            Toast.makeText(RegisterActivity.this,"学号/教师号不正确",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.getText().length()<6)
        {
            Toast.makeText(RegisterActivity.this,"密码太短啦",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(answers.getText().length()<1)
        {
            Toast.makeText(RegisterActivity.this,"请输入密保答案",Toast.LENGTH_SHORT).show();
            return;
        }

        String _username=user.getText().toString().trim();//账户
        Log.d("测试", "registerAndLogin: 账户名"+_username);
        String _passwords=password.getText().toString().trim();//密码
        Log.d("测试", "registerAndLogin: 密码"+_passwords);
        String _name=name.getText().toString().trim();//名字
        Log.d("测试", "registerAndLogin: 名字"+_name);
        String _answers=answers.getText().toString().trim();//密保答案
        Log.d("测试", "registerAndLogin: 密保答案"+_answers);
        Integer _mibaoProblem=mibaoMap.get(miBao.getSelectedItem().toString().trim());//密保问题
        Log.d("测试", "registerAndLogin: 密保问题"+_mibaoProblem);
        String _sex=sex.getSelectedItem().toString().trim();//性别
        Log.d("测试", "registerAndLogin: 性别"+_sex);
        Integer _isTeacher=(job.isChecked())?1:0;//是否是老师
        Log.d("测试", "registerAndLogin: 是否是老师"+_isTeacher);

        boolean susscesRegister=true;//注册是否成功
        User user = new User();
        user.setIsTeacher(_isTeacher);
        user.setNumber(_username);
        user.setName(_name);
        user.setSecretAnswer(_answers);
        user.setSecretId(_mibaoProblem);
        user.setSex(_sex);
        user.setPassword(_passwords);
        susscesRegister = registerPresenter.register(user);

        //TODO 后端->注册数据库，判断是否以有用户名，然后把susscesRegister返回
        //mibaoMap.get(_mibaoProblem)  把密保映射成int
        //begin
        Log.d("测试", "susscesRegister: 是否注册成功"+susscesRegister);
//        Student student = new Student();
//        student.setNumber(_username);
//        student.setPassword(_passwords);
//        registerPresenter.register(student);


        //end

        if(susscesRegister)
        {
            if(_isTeacher==1) {
                Intent intent = new Intent(RegisterActivity.this, TeacherMainActivity.class);
                intent.putExtra("user", _username);
                intent.putExtra("name", _name);
                intent.putExtra("isTeacher", _isTeacher);
                startActivity(intent);
                ActivityManager.getAppManager().finishActivity(LoginActivity.class);
                ActivityManager.getAppManager().finishActivity(RegisterActivity.this);
            }
            else
            {
                Intent intent = new Intent(RegisterActivity.this, StudentMainActivity.class);
                intent.putExtra("user", _username);
                intent.putExtra("name", _name);
                intent.putExtra("isTeacher", _isTeacher);
                startActivity(intent);
                ActivityManager.getAppManager().finishActivity(LoginActivity.class);
                ActivityManager.getAppManager().finishActivity(RegisterActivity.this);
            }
        }
        else
        {
            Toast.makeText(RegisterActivity.this,"账户名存在",Toast.LENGTH_SHORT).show();
        }
    }

    //输入框限制功能
    InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {

                int destLen = dest.toString().getBytes("GB18030").length;
                int sourceLen = source.toString().getBytes("GB18030").length;
                if (destLen + sourceLen > 16) {
                    Toast.makeText(RegisterActivity.this,"最多可以输入16个英文字母",Toast.LENGTH_SHORT).show();
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

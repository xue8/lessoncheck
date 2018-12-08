package term.rjb.x2l.lessoncheck.activity;

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

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.pojo.Student;
import term.rjb.x2l.lessoncheck.presenter.RegisterPresenter;

import java.io.UnsupportedEncodingException;

/**
 *
 */
public class RegisterActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private EditText password;
    private EditText user;
    RegisterPresenter registerPresenter;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerPresenter = new RegisterPresenter(this);

        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("注册");
        toolBar.setSubtitle("成为我们的一员");
        password=findViewById(R.id.et_password);
        register=findViewById(R.id.btn_registerAndLogin);
        register.setOnClickListener(onClickListener);
        user=findViewById(R.id.et_user);
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        password.setFilters(new InputFilter[]{inputFilter});
    }
    //顶部后退按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
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

        String username=user.getText().toString().trim();//账户
        String passwords=password.getText().toString().trim();//密码
        Student student = new Student();

        student.setNumber(username);
        student.setPassword(passwords);
        boolean susscesRegister=false;//注册是否成功

        registerPresenter.register(student);

        //TODO 后端交互->注册数据库，判断是否以有用户名
        //
        //

        if(susscesRegister)
        {
            //TODO 进入主界面
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
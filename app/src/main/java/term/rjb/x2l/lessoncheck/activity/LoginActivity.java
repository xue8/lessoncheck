package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.presenter.LoginPresenter;

import cn.bmob.v3.Bmob;
import term.rjb.x2l.lessoncheck.presenter.StudentPresenter;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private Button rigister;
    private Button login;
    private EditText password;
    private EditText user;
    LoginPresenter loginPresenter;
    String username;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Intent intent;
            String name="";
            switch (msg.what) {
                case 0:
                    Toast.makeText(LoginActivity.this,"登录失败，账号或者密码错误",Toast.LENGTH_SHORT).show();
                    break;
                case 1:

                    intent=new Intent(LoginActivity.this,StudentMainActivity.class);
                    intent.putExtra("user",username);
                    intent.putExtra("name",name);
                    intent.putExtra("isTeacher",1);
                    startActivity(intent);
                    ActivityManager.getAppManager().finishActivity(LoginActivity.this);

                    break;
                case 2:

                    intent=new Intent(LoginActivity.this,TeacherMainActivity.class);
                    intent.putExtra("user",username);
                    intent.putExtra("name",name);
                    intent.putExtra("isTeacher",1);
                    startActivity(intent);
                    ActivityManager.getAppManager().finishActivity(LoginActivity.this);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActivityManager.getAppManager().addActivity(this);

        loginPresenter = new LoginPresenter(this);
        Bmob.initialize(this, "fba4168e732413ce0d5404cf54a719db");

        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("登录");
        toolBar.setSubtitle("进入课堂签到");
        toolBar.setLogo(R.drawable.guet);
        setSupportActionBar(toolBar);

        rigister = findViewById(R.id.btn_rigister);
        login=findViewById(R.id.btn_login);
        password=findViewById(R.id.et_password);
        user=findViewById(R.id.et_user);

        rigister.setOnClickListener(onClickListener);
        login.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action1:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:

                    username=user.getText().toString().trim();//账户
                    String passwords=password.getText().toString().trim();//密码
                    int susscesRegister=0;//注册是否成功
                    loginPresenter.login(username,passwords, handler);
//                    switch (susscesRegister)
//                    {
//                        case 0:
//
//                            break;
//                        case 1:
//                            break;
//                        case 2:
//
//                            String name="";
//
//                            Intent intent=new Intent(LoginActivity.this,TeacherMainActivity.class);
//                            intent.putExtra("user",username);
//                            intent.putExtra("name",name);
//                            intent.putExtra("isTeacher",1);
//                            startActivity(intent);
//                            ActivityManager.getAppManager().finishActivity(LoginActivity.this);
//                            break;
//                    }
                    loginPresenter.login(username,passwords, handler);

                    String name="";
                    Intent intent;
                    //name
//                    switch (susscesRegister)
//                    {
//                        case 0:
//
//                            break;
//                        case 1:
//                             intent=new Intent(LoginActivity.this,StudentMainActivity.class);
//                            intent.putExtra("user",username);
//                            intent.putExtra("name",name);
//                            intent.putExtra("isTeacher",0);
//                            startActivity(intent);
//                            ActivityManager.getAppManager().finishActivity(LoginActivity.this);
//                            break;
//                        case 2:
//                             intent=new Intent(LoginActivity.this,TeacherMainActivity.class);
//                            intent.putExtra("user",username);
//                            intent.putExtra("name",name);
//                            intent.putExtra("isTeacher",1);
//                            startActivity(intent);
//                            ActivityManager.getAppManager().finishActivity(LoginActivity.this);
//                            break;
//                            default:
//                                break;
//                    }
                    break;
                case R.id.btn_rigister:
                    intent =new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}

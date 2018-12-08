package term.rjb.x2l.lessoncheck.presenter;

import android.util.Log;
import android.widget.Toast;

import term.rjb.x2l.lessoncheck.activity.LoginActivity;
import term.rjb.x2l.lessoncheck.activity.RegisterActivity;
import term.rjb.x2l.lessoncheck.pojo.Student;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginPresenter {
    private LoginActivity loginActivity;

    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

//    登录
    public void login(String number, String password){
        BmobUser userLogin = new BmobUser();
        userLogin.setUsername(number.toString());
        userLogin.setPassword(password);
        userLogin.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    Toast.makeText(loginActivity,bmobUser.getUsername()+"登录成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(loginActivity,"登录失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}

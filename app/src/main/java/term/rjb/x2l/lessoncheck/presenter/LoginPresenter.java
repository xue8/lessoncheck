package term.rjb.x2l.lessoncheck.presenter;

import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import term.rjb.x2l.lessoncheck.activity.LoginActivity;
import term.rjb.x2l.lessoncheck.pojo.Student;

public class LoginPresenter {
    private LoginActivity loginActivity;

    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

//    登录
    public void login(String number, String password){
        final Student student = new Student();
        student.setUsername(number);
        student.setPassword(password);
        student.login(new SaveListener<Student>() {
            @Override
            public void done(Student bmobStudent, BmobException e) {
                if(e==null){
                    Student student = BmobUser.getCurrentUser(Student.class);
                    Toast.makeText(loginActivity,student.getUsername()+"登录成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(loginActivity,"登录失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}

package term.rjb.x2l.lessoncheck.presenter;

import android.widget.Toast;

import term.rjb.x2l.lessoncheck.activity.LoginActivity;
import term.rjb.x2l.lessoncheck.activity.RegisterActivity;
import term.rjb.x2l.lessoncheck.pojo.Student;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterPresenter {
    private RegisterActivity registerActivity;

    public RegisterPresenter(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    //  注册
    public void register(Student student) {
        student.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(registerActivity,"注册成功",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(registerActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

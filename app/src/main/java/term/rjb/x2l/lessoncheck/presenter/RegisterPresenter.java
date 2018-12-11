package term.rjb.x2l.lessoncheck.presenter;

import android.util.Log;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import term.rjb.x2l.lessoncheck.activity.RegisterActivity;
import term.rjb.x2l.lessoncheck.pojo.Student;
import term.rjb.x2l.lessoncheck.pojo.Teacher;
import term.rjb.x2l.lessoncheck.pojo.User;

public class RegisterPresenter {
    private RegisterActivity registerActivity;
    boolean success = false;

    public RegisterPresenter(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    //  注册
    public boolean register(User user) {
        user.setUsername(user.getNumber());
        user.signUp(new SaveListener<Student>() {
            @Override
            public void done(Student bmobStudent, BmobException e) {
                if (e == null) {
                    Toast.makeText(registerActivity,"注册成功",Toast.LENGTH_SHORT).show();
                    success = true;
                } else {
                    Toast.makeText(registerActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
                    success = false;
                }
            }
        });
        return success;
    }

}

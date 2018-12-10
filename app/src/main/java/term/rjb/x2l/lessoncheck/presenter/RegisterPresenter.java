package term.rjb.x2l.lessoncheck.presenter;

import android.util.Log;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import term.rjb.x2l.lessoncheck.activity.RegisterActivity;
import term.rjb.x2l.lessoncheck.pojo.Student;
import term.rjb.x2l.lessoncheck.pojo.Teacher;

public class RegisterPresenter {
    private RegisterActivity registerActivity;
    boolean success = false;

    public RegisterPresenter(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    //  注册
    public boolean studentRegister(Student student) {
        student.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
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

    //  注册
    public boolean teacherRegister(Teacher teacher) {
        teacher.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
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

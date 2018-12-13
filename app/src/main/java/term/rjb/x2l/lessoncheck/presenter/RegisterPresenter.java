package term.rjb.x2l.lessoncheck.presenter;

import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import android.os.Handler;

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
    private Handler handler;

    public RegisterPresenter(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

    //  注册
    public void register(User user, Handler h) {
        handler = h;
        user.setUsername(user.getNumber());
        user.signUp(new SaveListener<Student>() {
            @Override
            public void done(Student bmobStudent, BmobException e) {
                if (e == null) {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        });
    }

}

package term.rjb.x2l.lessoncheck.presenter;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import term.rjb.x2l.lessoncheck.activity.LoginActivity;
import term.rjb.x2l.lessoncheck.pojo.Student;
import term.rjb.x2l.lessoncheck.pojo.User;

public class LoginPresenter {
    private LoginActivity loginActivity;
    private int job = 0;
    private Handler handler;
    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

//    登录
    public int login(String number, String password, Handler h){
        handler = h;
        final User user = new User();
        user.setUsername(number);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobStudent, BmobException e) {
                if(e==null){
                    User user1 = BmobUser.getCurrentUser(User.class);
//                    Toast.makeText(loginActivity,user1.getName()+"登录成功",Toast.LENGTH_SHORT).show();
                    if(user1.getIsTeacher() == 0){
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }else{
                        Message message = new Message();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                }else {
//                    Toast.makeText(loginActivity,"登录失败"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
        });
        return job;
    }



}

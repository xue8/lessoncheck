package term.rjb.x2l.lessoncheck.presenter;

import android.util.Log;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import term.rjb.x2l.lessoncheck.activity.TeacherActivity;
import term.rjb.x2l.lessoncheck.pojo.Class;

public class TeacherPresenter {
    private TeacherActivity teacherActivity;
    public TeacherPresenter(TeacherActivity teacherActivity){
        this.teacherActivity = teacherActivity;
    }

    public void createClass(Class c){
        c.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(teacherActivity,"创建成功",Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(teacherActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

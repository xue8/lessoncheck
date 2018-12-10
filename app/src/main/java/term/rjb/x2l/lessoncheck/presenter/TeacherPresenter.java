package term.rjb.x2l.lessoncheck.presenter;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import term.rjb.x2l.lessoncheck.activity.TeacherActivity;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.User;

public class TeacherPresenter {
    private TeacherActivity teacherActivity;
    private List<Lesson> lessons;
    public TeacherPresenter(TeacherActivity teacherActivity){
        this.teacherActivity = teacherActivity;
    }

    public List<Lesson> getAllClassByTeacherNumber() {
        BmobQuery<Lesson> bmobQuery = new BmobQuery<>();
        User user = BmobUser.getCurrentUser(User.class);
        bmobQuery.addWhereEqualTo("teacher", user);
        lessons = null;
        bmobQuery.findObjects(new FindListener<Lesson>() {
            @Override
            public void done(List<Lesson> objects, BmobException e) {
                if (e == null) {
                    lessons = objects;
                } else {
                    Log.d("BMOB", e.toString());
                }
            }
        });
        return lessons;
    }

    public void createClass(Lesson c) {
        c.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                        Toast.makeText(teacherActivity,"创建成功",Toast.LENGTH_SHORT).show();
                        Log.d("测试", "插入成功");

                    } else {
                        Toast.makeText(teacherActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("测试", "插入失败");
                    }
            }
        });
    }


//            User user1 = BmobUser.getCurrentUser(User.class);
//            Teacher_Lesson teacher_lesson = new Teacher_Lesson();
//            teacher_lesson.setNumber(user1.getNumber());
//            teacher_lesson.setLesson(c);
//            teacher_lesson.save(new SaveListener<String>() {
//                @Override
//                public void done(String objectId, BmobException e) {
//                    if (e == null) {
////                        Toast.makeText(teacherActivity,"创建成功",Toast.LENGTH_SHORT).show();
//                        Log.d("测试", "插入成功");
//
//                    } else {
////                        Toast.makeText(teacherActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
//                        Log.d("测试", "插入失败");
//                    }
//                }
//            });
//
//        }


    public void deleteClass(String objectId){
        Lesson c = new Lesson();
        c.setObjectId(objectId);
        c.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(teacherActivity,"删除成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(teacherActivity,"删除失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

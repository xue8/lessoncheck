package term.rjb.x2l.lessoncheck.presenter;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import term.rjb.x2l.lessoncheck.Utils.View;
import term.rjb.x2l.lessoncheck.activity.TeacherActivity;
import term.rjb.x2l.lessoncheck.activity.TeacherMainActivity;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Sign;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Student;
import term.rjb.x2l.lessoncheck.pojo.Sign_Student;
import term.rjb.x2l.lessoncheck.pojo.Student;
import term.rjb.x2l.lessoncheck.pojo.Teacher;
import term.rjb.x2l.lessoncheck.pojo.User;

public class TeacherPresenter {
    private View view;
    private List<Lesson> lessons;
    private List<User> students;
    private List<String> signNumbers;
    private List<Lesson_Student> lesson_students;
    private int studentNum;
    private int num1;
    private String objectId1;
    public TeacherPresenter(View view){
        this.view = view;
    }

    public void getAllClassByTeacherNumber() {
        BmobQuery<Lesson> bmobQuery = new BmobQuery<>();
        final User user = BmobUser.getCurrentUser(User.class);
        bmobQuery.addWhereEqualTo("teacher", user);
            bmobQuery.findObjects(new FindListener<Lesson>() {
                @Override
                public void done(List<Lesson> objects, BmobException e) {
                    if(e==null){
                        view.getALLessons(objects);
                         for(Lesson lesson : objects) {
                             Log.d("测试","lesson"+lesson.getLessonNumber());
//                             Lesson lesson1 = new Lesson();
//                             lesson1.setLessonNumber(lesson.getLessonNumber());
//                             lesson1.setLessonName(lesson.getLessonName());
//                             lesson1.setStudentNum(lesson.getStudentNum());
//                             lesson1.setTeacher(user);
//                             lessons.add(lesson1);
                        }

                        view.getALLessons(lessons);
                    } else {
                        Log.d("BMOB", e.toString());
                    }
                }
            });
    }

    public void createClass(Lesson c) {
        c.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                        Log.d("测试", "插入成功");

                    } else {
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

    public void updateStudentNum(int num,String objectId){
        studentNum = 0;
        num1 = num;
        objectId1 = objectId;
        BmobQuery<Lesson> bmobQuery = new BmobQuery<Lesson>();
        bmobQuery.addQueryKeys("studentNum");
        bmobQuery.addWhereEqualTo("objectId",objectId);
        bmobQuery.findObjects(new FindListener<Lesson>() {
            @Override
            public void done(List<Lesson> object, BmobException e) {
                if(e==null){
                    Lesson lesson = new Lesson();
                    studentNum= object.get(0).getStudentNum();
                    lesson.setStudentNum(studentNum+num1);
                    lesson.update(objectId1, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Log.d("测试", "课堂人数更新成功");
                            }else{
                                Log.d("测试", "课堂人数更新失败");
                            }
                        }

                    });
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void deleteClass(String objectId) {
        Lesson c = new Lesson();
        c.setObjectId(objectId);
        c.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
//                    Toast.makeText(teacherActivity, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(teacherActivity, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BmobQuery<Lesson_Student> bmobQuery = new BmobQuery<>();
        bmobQuery.addQueryKeys("objectId");
        bmobQuery.addWhereEqualTo("lessonObjectId", objectId);
        bmobQuery.findObjects(new FindListener<Lesson_Student>() {
            @Override
            public void done(List<Lesson_Student> object, BmobException e) {
                if (e == null) {
                    for (Lesson_Student lesson_student : object) {
                        lesson_student.delete(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Log.d("测试", "课堂学生表删除成功");
                                } else {
                                    Log.d("测试", "课堂学生表删除失败");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public List<Lesson_Student> getAllLessonStudents(String lessonObjectId){
        BmobQuery<Lesson_Student> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("lessonObjectId", lessonObjectId);
        bmobQuery.findObjects(new FindListener<Lesson_Student>() {
            @Override
            public void done(List<Lesson_Student> object, BmobException e) {
                if(e==null){
                    lesson_students = object;
                }else{
                    lesson_students = null;
                }
            }
        });
        return lesson_students;
    }

    public List<String> getSignByLesson(String lessonObjectId){
        BmobQuery<Lesson_Sign> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("lessonObjectId", lessonObjectId);
        bmobQuery.order("createAt");
        bmobQuery.findObjects(new FindListener<Lesson_Sign>() {
            @Override
            public void done(List<Lesson_Sign> list, BmobException e) {
                for(Lesson_Sign lesson_sign : list){
                    signNumbers.add(lesson_sign.getSignNumber());
                }
            }
        });
        return signNumbers;
    }

    public List<User> getSignedStudent(String signNumber){
        BmobQuery<Sign_Student> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("signNumber", signNumber);
        bmobQuery.findObjects(new FindListener<Sign_Student>() {
            @Override
            public void done(List<Sign_Student> list, BmobException e) {
                if(e==null){
                    for(Sign_Student sign_student : list){
                        students.add(sign_student.getStudent());
                    }
                }else{
                    students = null;
                }
            }
        });
        return students;
    }
}


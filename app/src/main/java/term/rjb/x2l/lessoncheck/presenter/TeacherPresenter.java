package term.rjb.x2l.lessoncheck.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import term.rjb.x2l.lessoncheck.Utils.View;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Sign;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Student;
import term.rjb.x2l.lessoncheck.pojo.Sign_Student;
import term.rjb.x2l.lessoncheck.pojo.User;

public class TeacherPresenter {
    private View view;

    private User student;
    public TeacherPresenter(View view){
        this.view = view;
    }

    /*
        获得教师所有的课堂
     */
    public void getAllClassByTeacherNumber(final Handler handler) {
        BmobQuery<Lesson> bmobQuery = new BmobQuery<>();
        final User user = BmobUser.getCurrentUser(User.class);
        bmobQuery.addWhereEqualTo("teacher", user);
            bmobQuery.findObjects(new FindListener<Lesson>() {
                @Override
                public void done(List<Lesson> objects, BmobException e) {
                    if(e==null){
                        Message message = new Message();
                        message.what = 0;
                        message.obj = objects;
                        handler.sendMessage(message);
                    } else {
                        Log.d("BMOB", e.toString());
                    }
                }
            });
    }

    /*
        创建课堂
     */

    public void createClass(Lesson c, final Handler handler) {
        c.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                        Message message = new Message();
                        message.what = 0;
                        message.obj = objectId;
                        handler.sendMessage(message);
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
//
//    public void updateStudentNum(int num,String objectId){
//        studentNum = 0;
//        num1 = num;
//        objectId1 = objectId;
//        BmobQuery<Lesson> bmobQuery = new BmobQuery<Lesson>();
//        bmobQuery.addQueryKeys("studentNum");
//        bmobQuery.addWhereEqualTo("objectId",objectId);
//        bmobQuery.findObjects(new FindListener<Lesson>() {
//            @Override
//            public void done(List<Lesson> object, BmobException e) {
//                if(e==null){
//                    Lesson lesson = new Lesson();
//                    studentNum= object.get(0).getStudentNum();
//                    lesson.setStudentNum(studentNum+num1);
//                    lesson.update(objectId1, new UpdateListener() {
//
//                        @Override
//                        public void done(BmobException e) {
//                            if(e==null){
//                                Log.d("测试", "课堂人数更新成功");
//                            }else{
//                                Log.d("测试", "课堂人数更新失败");
//                            }
//                        }
//
//                    });
//                }else{
//                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//                }
//            }
//        });
//
//
//    }

    /*
       删除课程
     */
    public void deleteClass(String objectId) {
        Lesson c = new Lesson();
        c.setObjectId(objectId);
        final BmobQuery<Lesson> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("objectId",objectId);
        bmobQuery.findObjects(new FindListener<Lesson>() {
            @Override
            public void done(List<Lesson> list, BmobException e) {
                if(e == null){
                    BmobQuery<Lesson_Student> bmobQuery1 = new BmobQuery<>();
                    bmobQuery1.addWhereEqualTo("lessonNumber",list.get(0).getLessonNumber());
                    bmobQuery1.findObjects(new FindListener<Lesson_Student>() {
                        @Override
                        public void done(List<Lesson_Student> list, BmobException e) {
                            if(e == null){
                                Lesson_Student lesson_student = new Lesson_Student();
                                for(Lesson_Student lesson_student1 : list) {
                                    lesson_student.setObjectId(lesson_student1.getObjectId());
                                    lesson_student.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e == null){
                                                Log.d("测试","删除课堂学生成功");
                                            }else{
                                                Log.d("测试","删除课堂学生失败");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
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

//        BmobQuery<Lesson_Student> bmobQuery = new BmobQuery<>();
//        bmobQuery.addQueryKeys("objectId");
//        bmobQuery.addWhereEqualTo("lessonNumber", objectId);
//        bmobQuery.findObjects(new FindListener<Lesson_Student>() {
//            @Override
//            public void done(List<Lesson_Student> object, BmobException e) {
//                if (e == null) {
//                    for (Lesson_Student lesson_student : object) {
//                        lesson_student.delete(new UpdateListener() {
//
//                            @Override
//                            public void done(BmobException e) {
//                                if (e == null) {
//                                    Log.d("测试", "课堂学生表删除成功");
//                                } else {
//                                    Log.d("测试", "课堂学生表删除失败");
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
    }
    /*
        获得某个课程里所有的学生
     */
    public void getAllLessonStudents(String lessonObjectId, final Handler handler){
        BmobQuery<Lesson_Student> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("lessonNumber", lessonObjectId);
        bmobQuery.include("student");
        bmobQuery.findObjects(new FindListener<Lesson_Student>() {
            @Override
            public void done(List<Lesson_Student> object, BmobException e) {
                if(e==null){
                    Log.d("测试","object size"+object.get(0).getLessonNumber());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = object;
                    handler.sendMessage(message);
                }else{

                }
            }
        });
    }
    /*
        获取某个课的所有签到信息
     */
    public void getSignByLesson(String lessonObjectId, final Handler handler){
        BmobQuery<Lesson_Sign> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("lessonNumber", lessonObjectId);
        bmobQuery.order("createAt");
        bmobQuery.findObjects(new FindListener<Lesson_Sign>() {
            @Override
            public void done(List<Lesson_Sign> list, BmobException e) {
                Message message = new Message();
                message.what = 0;
                message.obj = list;
                handler.sendMessage(message);
            }
        });
    }
    /*
        获取某次签到得学生签到情况
     */
    public void getSignedStudent(String signNumber, final Handler handler){
        BmobQuery<Sign_Student> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("signNumber", signNumber);
        bmobQuery.include("student");
        bmobQuery.findObjects(new FindListener<Sign_Student>() {
            @Override
            public void done(List<Sign_Student> list, BmobException e) {
                if(e==null){
                    Message message = new Message();
                    message.what = 0 ;
                    message.obj = list;
                    handler.sendMessage(message);
                }else{
                }
            }
        });
    }
    /*
        产生6位数大写字母+数字口令
     */
    public char[] getCode() {
        char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P','Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z','0','1','2','3','4','5','6','7','8','9'};
        boolean[] flags = new boolean[letters.length];
        char[] chs = new char[6];
        for (int i = 0; i < chs.length; i++) {
            int index;
            do {
                index = (int) (Math.random() * (letters.length));
            } while (flags[index]);// 判断生成的字符是否重复
            chs[i] = letters[index];
            flags[index] = true;
        }
        return chs;
    }
    /*
       发布二维码后台需要用到得数据：课号、持续时间
     */
    public void insertLessonCode(String lessonNumber, String signNumber, int lastMinute, BmobGeoPoint bmobGeoPoint, final Handler handler){
        Lesson_Sign lesson_sign = new Lesson_Sign();
        lesson_sign.setAddress(bmobGeoPoint);
        lesson_sign.setLastMinute(lastMinute);
        lesson_sign.setLessonNumber(lessonNumber);
        lesson_sign.setSignNumber(signNumber);
        lesson_sign.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    Message message = new Message();
                    message.what = 0;
                    message.obj = 1;
                    handler.sendMessage(message);
                }else{
                    e.getMessage();
                }
            }
        });
    }

    public void insertAllSigns(String lessonNumber, final String signNumber, final Handler handler){
         BmobQuery<Lesson_Student> bmobQuery = new BmobQuery<>();
         bmobQuery.include("student");
         bmobQuery.addWhereEqualTo("lessonNumber",lessonNumber);
         bmobQuery.findObjects(new FindListener<Lesson_Student>() {
             @Override
             public void done(List<Lesson_Student> list, BmobException e) {
                 if(e == null) {
                     if (list.size() > 0) {
                         for (Lesson_Student lesson_student : list) {
                             Sign_Student sign_student = new Sign_Student();
                             sign_student.setStudent(lesson_student.getStudent());
                             sign_student.setSignNumber(signNumber);
                             sign_student.save(new SaveListener<String>() {
                                 @Override
                                 public void done(String s, BmobException e) {
                                     Message message = new Message();
                                     message.what = 1;
                                     message.obj = 1;
                                     handler.sendMessage(message);
                                 }
                             });
                         }
                     }
                 }
             }
         });
    }

    public void getAllSignMessageByStudentNumber(final String lessonNumber, final String studentNum, final Handler handler){

        BmobQuery<User> bmobQuery2 = new BmobQuery<>();
        bmobQuery2.addWhereEqualTo("Number",studentNum);
        Log.d("测试","studentNum"+studentNum);
        bmobQuery2.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                Log.d("测试","User size"+list.size());
                student = list.get(0);
                final BmobQuery<Lesson_Sign> bmobQuery = new BmobQuery<>();
                bmobQuery.addWhereEqualTo("lessonNumber",lessonNumber);
                bmobQuery.findObjects(new FindListener<Lesson_Sign>() {
                    @Override
                    public void done(List<Lesson_Sign> list, BmobException e) {
                        Log.d("测试","lesson_sign size"+list.size());
                        if(e==null){
                            BmobQuery<Sign_Student> bmobQuery1;
                            BmobQuery<Sign_Student> bmobQuery3;
                            List<BmobQuery<Sign_Student>> queries = new ArrayList<>();
                            bmobQuery1  = new BmobQuery<>();
                            bmobQuery1.addWhereEqualTo("student",student);
                            Log.d("测试","student number"+student.getNumber());
                            List<String> list1 = new ArrayList<>();
                            StringBuilder stringBuilder = new StringBuilder();
                            for(Lesson_Sign lesson_sign : list){
                                String str = "'"+lesson_sign.getSignNumber()+"'"+",";
                                stringBuilder.append(str);
                            }
                            String bql = "select * from Sign_Student where student='"+student.getObjectId()+"' and signNumber in ("+ stringBuilder.substring(0,stringBuilder.lastIndexOf(","))+")";
                            Log.d("测试","bql="+bql);
                            new BmobQuery<Sign_Student>().doSQLQuery(bql,new SQLQueryListener<Sign_Student>(){

                                @Override
                                public void done(BmobQueryResult<Sign_Student> result, BmobException e) {
                                    if(e ==null){
                                        List<Sign_Student> list = (List<Sign_Student>) result.getResults();
                                        if(list!=null && list.size()>0){
                                            Message message = new Message();
                                            message.what = 6;
                                            message.obj = list;
                                            handler.sendMessage(message) ;

                                            Log.d("测试","sign_student size"+list.size());
                                        }else{
                                            Log.d("smile", "查询成功，无数据返回");
                                        }
                                    }else{
                                        Log.d("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });

    }
}


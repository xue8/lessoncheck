package term.rjb.x2l.lessoncheck.presenter;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.disposables.Disposable;
import term.rjb.x2l.lessoncheck.activity.LoginActivity;
import term.rjb.x2l.lessoncheck.activity.StudentMainActivity;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Sign;
import term.rjb.x2l.lessoncheck.pojo.Lesson_Student;
import term.rjb.x2l.lessoncheck.pojo.Sign_Student;
import term.rjb.x2l.lessoncheck.pojo.User;

public class StudentPresenter {
    BmobQuery<Lesson_Student> bmobQuery = new BmobQuery<>();
    BmobQuery<Lesson> bmobQuery_lesson = new BmobQuery<>();
    JSONArray lessonObjectId;
    private Handler handler;
    Lesson_Student lesson_student = new Lesson_Student();
    Lesson_Student lesson_student1 = new Lesson_Student();
    Sign_Student sign_student = new Sign_Student();
    String classSql = "";
    int studentNum;
    int num1;
    String objectId1;
    private BmobGeoPoint address_teacher;
    private BmobGeoPoint address_student;

    // 显示学生加入的所有课堂  message.what = 20
    public void getAllClassByStudentNumber(Handler h){
        this.handler = h;
        User user = BmobUser.getCurrentUser(User.class);
//        System.out.println(user.getAdd);
        // 获取LessonObjectId
        BmobQuery query =new BmobQuery("Lesson_Student");
        query.addWhereEqualTo("student", user.getObjectId());
        Log.e("getObjectId " , " " + user.getObjectId());
        query.setLimit(30);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(final JSONArray ary, BmobException e) {
                if(e==null){
                    String sql = "";
                    try{
                        Log.e("lessonNumber", "" + ary.getJSONObject(0).getString("lessonNumber"));
                    }catch (JSONException exx){

                    }
                    for (int i = 0; i <= ary.length(); i++){
                        try{
                            if( i== ary.length() -1 ){
                                sql = sql + "lessonNumber='" + ary.getJSONObject(i).getString("lessonNumber") + "'";
                            }else{
                                sql = sql + "lessonNumber='" + ary.getJSONObject(i).getString("lessonNumber") + "' or ";
                            }
                        }catch (JSONException e1){

                        }
                    }
                    String bql;
                    if( !sql.equals("")){
                        bql ="select * from Lesson where " + sql;
                    }else {
                        bql ="select * from Lesson where lessonNumber='2sdiashdusaiudhaiudhaiud'";
                    }
                    new BmobQuery<Lesson>().doSQLQuery(bql,new SQLQueryListener<Lesson>(){
                        @Override
                        public void done(BmobQueryResult<Lesson> result, BmobException e) {
                            if(e ==null){
                                List<Lesson> list = (List<Lesson>) result.getResults();
                                if(list!=null && list.size()>0){
                                    Message message = new Message();
                                    message.obj = list;
                                    message.what = 20;
                                    handler.sendMessage(message);
                                }else{
                                    Message message = new Message();
                                    message.obj = list;
                                    message.what = 20;
                                    handler.sendMessage(message);
                                    Log.i("smile", "查询成功，无数据返回");
                                }
                            }else{
                                Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    });


//                    for (int i = 0; i <= ary.length(); i++){
//                        try{
//                            System.out.println(ary.getJSONObject(i).getString("lessonObjectId"));
//
//                            BmobQuery queryLesson =new BmobQuery("Lesson");
//
//                            queryLesson.addWhereEqualTo("objectId", ary.getJSONObject(i).getString("lessonObjectId"));
//                            queryLesson.setLimit(30);
//                            queryLesson.findObjectsByTable(new QueryListener<JSONArray>() {
//                                @Override
//                                public void done(JSONArray ary, BmobException e) {
//                                    if(e==null){
//                                        for (int i = 0; i <= ary.length(); i++){
//                                            try{
////                                                System.out.println(ary.toString(i));
//                                                lessons.put(ary.toString(i));
//
//                                            }catch (JSONException e1){
//
//                                            }
//                                        }
////                                        LoginActivity loginActivity = new LoginActivity();
////                                        loginActivity.setA(lessons);
//                                        System.out.println("aaaaaaa");
//                                    }else{
//                                        Log.i("bmob","失败1："+e.getMessage()+","+e.getErrorCode());
//                                    }
//                                }
//                            });
//
//                        }catch (JSONException e1){
//
//                        }
//                    }

                }else{
                    Log.i("bmob","失败2："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    // 加入课堂 message.what = 19 加入成功  message.what = 18 加入失败 课堂ID不存在
    public void joinClassByLessonId(final String lessonId, Handler h) {
        handler = h;
        User user = BmobUser.getCurrentUser(User.class);
        lesson_student.setStudent(user);
        lesson_student.setLessonNumber(lessonId);

        String bql ="select * from Lesson where lessonNumber = '" + lessonId + "'";
        new BmobQuery<Lesson>().doSQLQuery(bql,new SQLQueryListener<Lesson>(){
            @Override
            public void done(BmobQueryResult<Lesson> result, BmobException e) {
                if(e ==null){
                    final List<Lesson> list = (List<Lesson>) result.getResults();
                    if(list!=null && list.size()>0){
                        lesson_student.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    //课堂人数 + 1
                                    updateStudentNum(1, list.get(0).getObjectId());
                                    Message message = new Message();
                                    message.obj = list.get(0);
                                    message.what = 19;
                                    handler.sendMessage(message);
                                    Log.d("测试", "插入成功");

                                } else {
                                    Log.d("测试", "插入失败");
                                }
                            }
                        });

                    }else{
                        Message message = new Message();
                        message.obj = "课堂id不存在！";
                        message.what = 18;
                        handler.sendMessage(message);

                        Log.i("smile", "查询成功，无数据返回");
                    }
                }else{
                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
    }

    // 退出课堂
    public void outClassByLessonId(final String lessonId, Handler h){

        String bql ="select * from Lesson_Student where lessonNumber='" + lessonId + "'";
        new BmobQuery<Lesson_Student>().doSQLQuery(bql,new SQLQueryListener<Lesson_Student>(){
            @Override
            public void done(BmobQueryResult<Lesson_Student> result, BmobException e) {
                if(e ==null){
                    final List<Lesson_Student> list = (List<Lesson_Student>) result.getResults();
                    if(list!=null && list.size()>0){
                        lesson_student.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {

                                    String bql1 ="select * from Lesson where lessonNumber='" + lessonId + "'";
                                    new BmobQuery<Lesson>().doSQLQuery(bql1,new SQLQueryListener<Lesson>(){
                                        @Override
                                        public void done(BmobQueryResult<Lesson> result, BmobException e) {
                                            if(e ==null){
                                                List<Lesson> list1 = (List<Lesson>) result.getResults();
                                                if(list1!=null && list1.size()>0){
                                                    // 课堂人数-1
                                                    updateStudentNum(-1, list1.get(0).getObjectId());
                                                    lesson_student1.setObjectId(list.get(0).getObjectId());
                                                    lesson_student1.delete(new UpdateListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if(e==null){
                                                                Log.d("测试", "退出成功");
                                                            }else{
                                                                Log.d("测试", "退出失败");
                                                            }
                                                        }
                                                    });
                                                    Log.d("测试", "插入成功");

                                                }else{
                                                    Log.i("smile", "查询成功，无数据返回");
                                                }
                                            }else{
                                                Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                                            }
                                        }
                                    });

                                } else {
                                    Log.d("测试", "插入失败");
                                }
                            }
                        });

                    }else{
                        Message message = new Message();
                        message.obj = "课堂id不存在！";
                        message.what = 18;
                        handler.sendMessage(message);

                        Log.i("smile", "查询成功，无数据返回");
                    }
                }else{
                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });
    }

    // 签到 message.what = 17 签到成功  message.what = 16 签到失败 message.what = 18;口令不正确
    public void setSignBySignNumber(String signNumber, String classNum,Handler h){
        System.out.println("signNumber" + signNumber);
        System.out.println("classNum" + classNum);
        final String sign = signNumber;
        handler = h;
        final User user = BmobUser.getCurrentUser(User.class);
        sign_student.setStudent(user);
        sign_student.setSignNumber(signNumber);

        String bql ="select * from Lesson_Sign where signNumber = '" + signNumber + "' and lessonNumber='" +  classNum +  "' order by createdAt desc";
        new BmobQuery<Lesson_Sign>().doSQLQuery(bql,new SQLQueryListener<Lesson_Sign>(){
            @Override
            public void done(BmobQueryResult<Lesson_Sign> result, BmobException e) {
                if(e ==null){
                    List<Lesson_Sign> list = (List<Lesson_Sign>) result.getResults();
                    if(list!=null && list.size()>0){
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        Date nowDate = new Date();

                        Calendar rightNow = Calendar.getInstance();
                        Date createdAt = new Date();
                        try{
                            createdAt = df.parse(list.get(0).getCreatedAt());
                        }catch (ParseException e1){

                        }
                        rightNow.setTime(createdAt);
                        rightNow.add(Calendar.MINUTE, list.get(0).getLastMinute());
                        // 获取老师发起签到的经纬度
                        address_teacher = list.get(0).getAddress();
                        // 获取学生的经纬度
                        address_student = user.getAddress();
                        System.out.println("address_teacher " + address_teacher.getLongitude() + "--" + address_teacher.getLatitude());
                        System.out.println("address_student " + address_student.getLongitude() + "--" + address_student.getLatitude());
                        System.out.println( " distanceInKilometersTo " + address_teacher.distanceInKilometersTo(address_student));
                        if( nowDate.before(rightNow.getTime()) ){
                            String bql ="select * from Sign_Student where signNumber='" + sign + "' and student='" + user.getObjectId() + "' order by createdAt desc";
                            new BmobQuery<Sign_Student>().doSQLQuery(bql,new SQLQueryListener<Sign_Student>() {
                                        @Override
                                        public void done(BmobQueryResult<Sign_Student> result, BmobException e) {
                                            if(e ==null){
                                                final List<Sign_Student> list = (List<Sign_Student>) result.getResults();
                                                if(list!=null && list.size()>0){
                                                    sign_student.setIsSign(1);
                                                    System.out.println("list.get(0).getObjectId()" + list.get(0).getObjectId());

                                                    // 判断学生签到经纬度是否在允许的范围内
                                                    if( address_teacher.distanceInKilometersTo(address_student) < 1 ){
                                                        sign_student.update(list.get(0).getObjectId(), new UpdateListener() {
                                                            @Override
                                                            public void done(BmobException e) {
                                                                if (e == null) {
                                                                    Message message = new Message();
                                                                    message.obj = "签到成功";
                                                                    message.what = 17;
                                                                    handler.sendMessage(message);
                                                                    Log.d("测试", "插入成功" + user.getObjectId());
                                                                } else {
                                                                    Message message = new Message();
                                                                    message.obj = "签到成功";
                                                                    message.what = 17;
                                                                    handler.sendMessage(message);
                                                                    Log.d("测试", "插入成功" + user.getObjectId());
                                                                }
                                                            }
                                                        });
                                                    }else{
                                                        Message message = new Message();
                                                        message.obj = "签到失败，不在有效范围内签到！";
                                                        message.what = 21;
                                                        handler.sendMessage(message);
                                                        Log.d("测试", "插入失败" + user.getObjectId());
                                                    }
                                                }else{
                                                    System.out.println("aaaaaaaaaa");
                                                }
                                            }else {
                                                System.out.println("aaaaaaaaaa" + e.getMessage());
                                            }
                                        }
                                    });

                        }else{
                            Message message = new Message();
                            message.obj = "签到失败";
                            message.what = 15;
                            handler.sendMessage(message);
                            System.out.println("签到失败，已过签到时间");
                        }
                    }else{
                        Message message = new Message();
                        message.obj = "课堂id不存在！";
                        message.what = 18;
                        handler.sendMessage(message);

                        Log.i("smile", "查询成功，无数据返回");
                    }
                }else{
                    Message message = new Message();
                    message.obj = "签到失败";
                    message.what = 15;
                    handler.sendMessage(message);
                    System.out.println("签到口令有误");

                    Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                }
            }
        });


    }

    // 获取签到情况
    public void getSignStatusByLessonId(String lessonNumber, Handler h){
        handler = h;
        final User user = BmobUser.getCurrentUser(User.class);

        BmobQuery query =new BmobQuery("Lesson_Sign");
        query.addWhereEqualTo("lessonNumber", lessonNumber);
        query.setLimit(30);
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if(e==null){
                    for (int i = 0; i <= ary.length(); i++){
                        try{
                            if( i== ary.length() -1 ){
                                classSql = classSql + "signNumber='" + ary.getJSONObject(i).getString("signNumber") + "'";
                            }else{
                                classSql = classSql + "signNumber='" + ary.getJSONObject(i).getString("signNumber") + "' or ";
                            }
                        }catch (JSONException e1){

                        }
                    }
                    System.out.println("sql + " + classSql);
                    String bql;
                    if( !classSql.equals("")){
                        bql ="select * from Sign_Student where student='" + user.getObjectId() + "' and " + classSql;
                    }else {
                        bql ="select * from Sign_Student where signNumber='2sdiashdusaiudhaiudhaiud'";
                    }
                    new BmobQuery<Sign_Student>().doSQLQuery(bql,new SQLQueryListener<Sign_Student>(){
                        @Override
                        public void done(BmobQueryResult<Sign_Student> result, BmobException e) {
                            if(e ==null){
                                List<Sign_Student> list = (List<Sign_Student>) result.getResults();
                                if(list!=null && list.size()>0){
                                    Message message = new Message();
                                    message.obj = list;
                                    message.what = 20;
                                    handler.sendMessage(message);
                                }else{
                                    Message message = new Message();
                                    message.obj = list;
                                    message.what = 19;
                                    handler.sendMessage(message);
                                    Log.i("smile", "查询成功，无数据返回");
                                }
                            }else{
                                Log.i("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    });
                }else{
                    Log.i("bmob","失败2："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    // 课堂人数 + 1 -1
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


    }

}

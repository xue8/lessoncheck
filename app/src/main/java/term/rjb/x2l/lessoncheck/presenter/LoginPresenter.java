package term.rjb.x2l.lessoncheck.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import term.rjb.x2l.lessoncheck.Utils.GPSUtils;
import term.rjb.x2l.lessoncheck.Utils.QRCodeUtil;
import term.rjb.x2l.lessoncheck.activity.LoginActivity;
import term.rjb.x2l.lessoncheck.activity.UniversalStudentCheckActivity;
import term.rjb.x2l.lessoncheck.pojo.Student;
import term.rjb.x2l.lessoncheck.pojo.User;

public class LoginPresenter {
    private LoginActivity loginActivity;
    private int job = 0;
    private Handler handler;
    private GPSUtils gpsUtils;
    String address;
    public LoginPresenter(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

//    登录 1学生 2老师
    public int login(String number, String password, Handler h){
        gpsUtils = new GPSUtils();
        handler = h;
        final User user = new User();
        user.setUsername(number);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User bmobStudent, BmobException e) {
                if(e==null){
                    final User user1 = BmobUser.getCurrentUser(User.class);
                    int LOCATION_CODE = 39;
                    if(user1.getIsTeacher() == 0){
                        // 更新用户坐标
                        //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
                        if (ContextCompat.checkSelfPermission(loginActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(loginActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //请求权限
                            ActivityCompat.requestPermissions(loginActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                            address = gpsUtils.getLocation(loginActivity);
                            String [] address1 = address.split(",");

                            double d = Double.parseDouble(address1[0]);
                            BigDecimal b = new BigDecimal(d);
                            d = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();

                            double d1 = Double.parseDouble(address1[1]);
                            BigDecimal b1 = new BigDecimal(d1);
                            d1 = b1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();


                            Double x = d1;
                            Double y = d;
                            BmobGeoPoint bmobGeoPoint = new BmobGeoPoint(x,y);
                            user1.setAddress(bmobGeoPoint);
                            user1.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Message message = new Message();
                                        message.obj = user1.getName();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                        Log.e("BMOB", "更新成功：" + user.getAddress().getLatitude());
//                                    Snackbar.make(mBtnUpdateLocation, "更新成功：" + user.getAddress().getLatitude() + "-" + user.getAddress().getLongitude(), Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Log.e("BMOB", e.toString());
//                                    Snackbar.make(mBtnUpdateLocation, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            address = gpsUtils.getLocation(loginActivity);
                            String [] address1 = address.split(",");

                            double d = Double.parseDouble(address1[0]);
                            BigDecimal b = new BigDecimal(d);
                            d = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();

                            double d1 = Double.parseDouble(address1[1]);
                            BigDecimal b1 = new BigDecimal(d1);
                            d1 = b1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();


                            Double x = d1;
                            Double y = d;
                            BmobGeoPoint bmobGeoPoint = new BmobGeoPoint(x,y);
                            user1.setAddress(bmobGeoPoint);
                            user1.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Message message = new Message();
                                        message.obj = user1.getName();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                        Log.e("BMOB", "更新成功：" + user.getAddress().getLatitude());
//                                    Snackbar.make(mBtnUpdateLocation, "更新成功：" + user.getAddress().getLatitude() + "-" + user.getAddress().getLongitude(), Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Log.e("BMOB", e.toString());
//                                    Snackbar.make(mBtnUpdateLocation, e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                    }else{
                        Message message = new Message();
                        message.obj = user1.getName();
                        message.what = 2;
                        handler.sendMessage(message);
                    }
                }else {
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
        });
        return job;
    }



}

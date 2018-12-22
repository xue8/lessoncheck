package term.rjb.x2l.lessoncheck.Utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import java.math.BigDecimal;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import term.rjb.x2l.lessoncheck.activity.StudentMainActivity;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.User;

/**
 * Created by Administrator on 2018/4/17.
 * 获取用户的地理位置
 */
public class GPSUtils {
    private LocationManager locationManager;
    private Location location;
    private GPSUtils gpsUtils;
    String address;
    private String locationProvider;       //位置提供器

    public String getLocation(Context context){
        String address = "";

        //1.获取位置管理器
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)){
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        }else if (providers.contains(LocationManager.GPS_PROVIDER)){
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        }else {
//            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return "error";
        }

        //3.获取上次的位置，一般第一次运行，此值为null
        try{
            location = locationManager.getLastKnownLocation(locationProvider);
        }catch (SecurityException e){

        }

        if (location!=null){
            address = location.getLatitude()+","+location.getLongitude();
//            showLocation(location);
        }else{
            // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
            try{
                locationManager.requestLocationUpdates(locationProvider, 0, 0,mListener);
            }catch (SecurityException e){

            }
        }
        return address;
    }

    public void showLocation(Location location){
        String address = "纬度："+location.getLatitude()+"经度："+location.getLongitude();
        System.out.println("----" + "纬度："+location.getLatitude()+"经度："+location.getLongitude());
//        mLocation.setText(address);
    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
        // 如果位置发生变化，重新显示
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }
    };

    public void setAddress(StudentMainActivity studentMainActivity){

        final User user1 = BmobUser.getCurrentUser(User.class);
        int LOCATION_CODE = 39;
        if(user1.getIsTeacher() == 0){
            // 更新用户坐标
            //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
            if (ContextCompat.checkSelfPermission(studentMainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(studentMainActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(studentMainActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                address = gpsUtils.getLocation(studentMainActivity);
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
                            Log.e("BMOB", "更新成功：" + user1.getAddress().getLatitude());
                        } else {
                            Log.e("BMOB", e.toString());
                        }
                    }
                });
            } else {
                address = gpsUtils.getLocation(studentMainActivity);
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
                            Log.e("BMOB", "更新成功：" + user1.getAddress().getLatitude());
                        } else {
                            Log.e("BMOB", e.toString());
                        }
                    }
                });
            }

        }else{
        }

    }

}

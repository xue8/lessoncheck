package term.rjb.x2l.lessoncheck.Utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;


import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 * 获取用户的地理位置
 */
public class GPSUtils {
    private LocationManager locationManager;
    private Location location;
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

}

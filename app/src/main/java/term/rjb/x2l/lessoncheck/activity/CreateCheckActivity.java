package term.rjb.x2l.lessoncheck.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;
import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.Utils.GPSUtils;
import term.rjb.x2l.lessoncheck.Utils.QRCodeUtil;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.presenter.TeacherPresenter;

public class CreateCheckActivity extends AppCompatActivity implements term.rjb.x2l.lessoncheck.Utils.View {
    private Toolbar toolBar;
    private Spinner time;
    private int timer;
    private Button create;
    private Button getLocal;
    private GPSUtils gpsUtils;
    private BmobGeoPoint bmobGeoPoint;
    private EditText et_name;
    private int flag1 = 0;
    private int flag2 = 0;
    private String codeStr;
    private String address;
    private TeacherPresenter teacherPresenter;
    private Intent intent;
    private Handler handler = new Handler(){
      public void handleMessage(Message message){
          switch (message.what){
              case 0: flag1 = (int)message.obj; break;
              case 1: flag2 = (int)message.obj;
          }

      }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getLocal:
                    Log.d("测试","地图图标点击");
                    getLocal();
                    break;
                case R.id.btn_create:
                    createCheck();
                    ActivityManager.getAppManager().finishActivity(CreateCheckActivity.this);
                    break;
            }
            if(flag1 == 1 && flag2 == 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(CreateCheckActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //没有权限则申请权限
                        ActivityCompat.requestPermissions(CreateCheckActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        //有权限直接执行,docode()不用做处理
                        String path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + "/qr_code" + codeStr + ".png";
                        Bitmap bitmap = QRCodeUtil.createQRImage(codeStr, 300, 300, null, path);
                        Toast.makeText(CreateCheckActivity.this, "二维码生成成功，已保存在您的相册！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //小于6.0，不用申请权限，直接执行
                    String path = Environment.getExternalStorageDirectory().getPath()+"/DCIM/" + "/qr_code" + codeStr + ".png";
                    Bitmap bitmap = QRCodeUtil.createQRImage(codeStr, 300, 300, null, path);
                    Toast.makeText(CreateCheckActivity.this,"二维码生成成功，已保存在您的相册！",Toast.LENGTH_SHORT).show();



                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_check);
        ActivityManager.getAppManager().addActivity(this);
        toolBar = this.findViewById(R.id.toolbar);
        create = this.findViewById(R.id.btn_create);
        getLocal = this.findViewById(R.id.btn_getLocal);
        et_name = this.findViewById(R.id.et_name);
        toolBar.setTitle("发布签到");
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTimer();
        create.setOnClickListener(onClickListener);
        getLocal.setOnClickListener(onClickListener);

    }


    void getLocal()
    {
        int LOCATION_CODE = 39;
        gpsUtils = new GPSUtils();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
        } else {
            address = gpsUtils.getLocation(CreateCheckActivity.this);
            String[] address1 = address.split(",");

            double d = Double.parseDouble(address1[0]);
            BigDecimal b = new BigDecimal(d);
            d = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();

            double d1 = Double.parseDouble(address1[1]);
            BigDecimal b1 = new BigDecimal(d1);
            d1 = b1.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();


            Double x = d1;
            Double y = d;
            bmobGeoPoint = new BmobGeoPoint(x, y);
            et_name.setText(x+" "+y);
            Log.d("测试","x+y"+x+","+y);
        }
    }

    void createCheck()
    {
        Intent intent=getIntent();
        String classNum=intent.getStringExtra("classNumber");
        //TODO 后端->二维码生成 把生成的二维码和签到口令传到数据库
        teacherPresenter = new TeacherPresenter(this);
        char[] code = teacherPresenter.getCode();
        codeStr = String.valueOf(code);
        Log.d("测试","LessonNumber"+classNum);
        teacherPresenter.insertLessonCode(classNum,codeStr,timer,bmobGeoPoint,handler);
        teacherPresenter.insertAllSigns(classNum,codeStr,handler);
        //timer时间
        //classNum课号
    }

    void initTimer()
    {
        time=findViewById(R.id.sp_time);
        switch (time.getSelectedItem().toString().trim())
        {
            case "5分钟":
                timer=5;
                break;
            case "10分钟":
                timer=10;
                break;
            case "15分钟":
                timer=15;
                break;
        }
    }
    //顶部后退按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityManager.getAppManager().finishActivity(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getALLessons(List<Lesson> lessons) {

    }
}

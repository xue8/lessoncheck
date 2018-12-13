package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;

public class CreateCheckActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private Spinner time;
    private Integer timer;
    private Button create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_check);
        ActivityManager.getAppManager().addActivity(this);
        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("发布签到");
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTimer();
        create.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_getLocal:
                    getLocal();
                    break;
                case R.id.btn_create:
                    createCheck();
                    ActivityManager.getAppManager().finishActivity(CreateCheckActivity.this);
                    break;
            }
        }
    };

    void getLocal()
    {
        //TODO 后端->获得当前地址
    }

    void createCheck()
    {
        Intent intent=getIntent();
        String classNum=intent.getStringExtra("classNum");
        //TODO 后端->二维码生成 把生成的二维码和签到口令传到数据库

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
}

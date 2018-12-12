package term.rjb.x2l.lessoncheck.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;

public class StudentCheckActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private Button check;
    private EditText key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check);
        ActivityManager.getAppManager().addActivity(StudentCheckActivity.this);
        toolBar = this.findViewById(R.id.toolbar);
        toolBar.setTitle("口令签到");
        setSupportActionBar(toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        check=findViewById(R.id.btn_check);
        key=findViewById(R.id.et_key);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ActivityManager.getAppManager().finishActivity(StudentCheckActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }
    void check()
    {
        //TODO 后端->验证签到码是否存在 检验时间是否过期 并且检验时间  如果都为真则发签到信息到数据库
        //key.getText().toString().trim(); 签到口令
    }
    //输入框限制功能
    InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {

                int destLen = dest.toString().getBytes("GB18030").length;
                int sourceLen = source.toString().getBytes("GB18030").length;
                if (destLen + sourceLen > 6) {
                    Toast.makeText(StudentCheckActivity.this,"最多可以输入6个英文字母",Toast.LENGTH_SHORT).show();
                    return "";
                }
                //如果按返回键
                if (source.length() < 1 && (dend - dstart >= 1)) {
                    return dest.subSequence(dstart, dend - 1);
                }
                return source;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
    };
}

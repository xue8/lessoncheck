package term.rjb.x2l.lessoncheck.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.Utils.GPSUtils;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.MyListAdapter;
import term.rjb.x2l.lessoncheck.pojo.TheClass;
import term.rjb.x2l.lessoncheck.pojo.User;
import term.rjb.x2l.lessoncheck.presenter.StudentPresenter;
import term.rjb.x2l.lessoncheck.presenter.TeacherPresenter;

public class StudentMainActivity extends AppCompatActivity {
    private TextView hello;
    private String username;
    private String name;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private Boolean deleteMode = false;
    private List<TheClass> list = new ArrayList<>();
    private MyListAdapter myListAdapter;
    StudentPresenter studentPresenter = new StudentPresenter();

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 20:
                    List<Lesson> list_l = (List<Lesson>)msg.obj;
                    System.out.println(list_l.size() + "xxxx");
                    list.clear();
                    for (int i = 0; i < list_l.size(); i++){
                        System.out.println( "xxsxxxx" + list_l.get(i).getLessonName());
                        Integer a = list_l.get(i).getStudentNum();

                        list.add(new TheClass(list_l.get(i).getTeacherName(),list_l.get(i).getLessonName(),list_l.get(i).getLessonNumber(),list_l.get(i).getStudentNum(),list_l.get(i).getObjectId()));
                    }
                    //样例
//                    list.add(new TheClass("秦xg","android实验","1234567",120,"666"));

                    //让布局显示
                    myListAdapter = new MyListAdapter(list);
                    myListAdapter.setItemClickListener(new MyListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String id) {
                           // Toast.makeText(StudentMainActivity.this, "进入课号" + id, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StudentMainActivity.this, UniversalStudentCheckActivity.class);
                            intent.putExtra("classNum",id);
                            intent.putExtra("studentNum",username);
                            intent.putExtra("isTeacher",0);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(String id) {
                            if (!deleteMode)
                                return;

                           // Toast.makeText(StudentMainActivity.this, "退出课号" + id, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getClassNumber().equals(id)) {
                                    //用到的数据
                                    //username 学生用户名
                                    //id 课堂代码
                                    studentPresenter.outClassByLessonId(id,handler);
                                    //begin
                                    //end
                                    list.remove(i);
                                }
                            }
                            myListAdapter.notifyDataSetChanged();
                        }
                    });
                    mRecyclerView.setAdapter(myListAdapter);
//                    GPSUtils gpsUtils = new GPSUtils();
//                    gpsUtils.setAddress(StudentMainActivity.this);
                    // 更新用户坐标
                    //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
                    GPSUtils gpsUtils = new GPSUtils();
                    String address;
                    final User user1 = BmobUser.getCurrentUser(User.class);
                    int LOCATION_CODE = 39;

                    if (ContextCompat.checkSelfPermission(StudentMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(StudentMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        //请求权限
                        ActivityCompat.requestPermissions(StudentMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                        address = gpsUtils.getLocation(StudentMainActivity.this);
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
                        address = gpsUtils.getLocation(StudentMainActivity.this);
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

                    break;
                case 19:

                    break;
            }
        }
    };
    public void addClass(TheClass target) {
        list.add(target);
        myListAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        ActivityManager.getAppManager().addActivity(this);
        mRecyclerView = findViewById(R.id.list_class);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        navigationView = findViewById(R.id.navigation_view);
        View headerLayout = navigationView.getHeaderView(0);
        drawerLayout = findViewById(R.id.layout_draw);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        hello = headerLayout.findViewById(R.id.tv_nav_txt);
        Intent intent = getIntent();
        username = intent.getStringExtra("user");
        name = intent.getStringExtra("name");
        hello.setText(String.format(username + "\n" + "你好 " + name + "同学"));
        setListener();
        MyClassInit();

    }
    //给左侧菜单添加点击事件
    private void setListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_nav_menu_my_class:
                        myListAdapter.notifyDataSetChanged();
                        break;
                    case R.id.item_nav_menu_delete_class:
                        deleteMode = true;
                        getSupportActionBar().setTitle("退出课堂");
                        Toast.makeText(StudentMainActivity.this, "长按卡片退出你的课堂", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_nav_menu_quit:
                        ActivityManager.getAppManager().exitApp(getApplicationContext());
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_teacher_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action1:
                Log.d("Student", "加入课堂");
                Intent intent = new Intent(StudentMainActivity.this, AddClassActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return true;
            case R.id.action2:
                MyClassInit();
                Toast.makeText(StudentMainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                Log.d("Student", "呼出菜单");
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    void MyClassInit() {

        // 获取加入的全部课堂
        studentPresenter.getAllClassByStudentNumber(handler);


        deleteMode = false;
        getSupportActionBar().setTitle("我的课堂");
        //username 学生用户名
        //begin

//        //样例
//        list.add(new TheClass("秦xg","android实验","1234567",120,"666"));
//
//        //让布局显示
//        myListAdapter = new MyListAdapter(list);
//        myListAdapter.setItemClickListener(new MyListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String id) {
//                Toast.makeText(StudentMainActivity.this, "进入课号" + id, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(StudentMainActivity.this, UniversalStudentCheckActivity.class);
//                intent.putExtra("classNum",id);
//                intent.putExtra("studentNum",username);
//                intent.putExtra("isTeacher",0);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(String id) {
//                if (!deleteMode)
//                    return;
//
//                Toast.makeText(StudentMainActivity.this, "退出课号" + id, Toast.LENGTH_SHORT).show();
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).getClassNumber().equals(id)) {
//                        //用到的数据
//                        //username 学生用户名
//                        //id 课堂代码
//
//                        //begin
//                        //end
//                        list.remove(i);
//                    }
//                }
//                myListAdapter.notifyDataSetChanged();
//            }
//        });
//        mRecyclerView.setAdapter(myListAdapter);

    }

}

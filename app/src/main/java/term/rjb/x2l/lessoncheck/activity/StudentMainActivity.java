package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
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

import java.util.ArrayList;
import java.util.List;

import term.rjb.x2l.lessoncheck.R;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.MyListAdapter;
import term.rjb.x2l.lessoncheck.pojo.TheClass;
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
                myListAdapter.notifyDataSetChanged();
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

        deleteMode = false;
        getSupportActionBar().setTitle("我的课堂");
        //TODO 后端->查找学生加入的所有课堂，new TheClass(老师名、课堂名、课堂代码、总学生数)。
        //username 学生用户名
        //begin

        //样例
        list.add(new TheClass("秦xg","android实验","1234567",120,"666"));

        //让布局显示
        myListAdapter = new MyListAdapter(list);
        myListAdapter.setItemClickListener(new MyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                Toast.makeText(StudentMainActivity.this, "进入课号" + id, Toast.LENGTH_SHORT).show();
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

                Toast.makeText(StudentMainActivity.this, "退出课号" + id, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getClassNumber().equals(id)) {
                        //TODO 后端->退出课堂  删除学生和课堂的联系
                        //用到的数据
                        //username 学生用户名
                        //id 课堂代码

                        //begin
                        //end
                        list.remove(i);
                    }
                }
                myListAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(myListAdapter);

    }

}

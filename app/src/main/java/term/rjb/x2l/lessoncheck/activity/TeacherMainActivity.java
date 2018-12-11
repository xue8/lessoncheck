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

import term.rjb.x2l.lessoncheck.R;
import java.util.List;
import term.rjb.x2l.lessoncheck.manager.ActivityManager;
import term.rjb.x2l.lessoncheck.pojo.MyListAdapter;
import term.rjb.x2l.lessoncheck.pojo.TheClass;

public class TeacherMainActivity extends AppCompatActivity {
    private TextView hello;
    private String username;
    private String name;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private MyListAdapter myListAdapter;
    private Boolean deleteMode=false;
    private List<TheClass> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(term.rjb.x2l.lessoncheck.R.layout.activity_teacher_main);
        ActivityManager.getAppManager().addActivity(this);
        mRecyclerView=findViewById(term.rjb.x2l.lessoncheck.R.id.list_class);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        navigationView=findViewById(R.id.navigation_view);
        View headerLayout = navigationView.getHeaderView(0);
        drawerLayout=findViewById(R.id.layout_draw);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        hello= headerLayout.findViewById(R.id.tv_nav_txt);
        Intent intent=getIntent();
        username= intent.getStringExtra("user");
        name=intent.getStringExtra("name");
        hello.setText(String.format(username+"\n"+"你好 "+name+"老师"));
        setListener();
        MyClassInit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        myListAdapter.notifyDataSetChanged();
    }

    //给左侧菜单添加点击事件
    private void setListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_nav_menu_my_class:
                        MyClassInit();
                        break;
                    case R.id.item_nav_menu_delete_class:
                        deleteMode=true;
                        getSupportActionBar().setTitle("删除课堂");
                        Toast.makeText(TeacherMainActivity.this, "长按卡片删除你的课堂", Toast.LENGTH_SHORT).show();
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
                Log.d("Teacher", "添加课堂");
                Intent intent=new Intent(TeacherMainActivity.this,CreateClassActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
                return true;
            case R.id.action2:
                myListAdapter.notifyDataSetChanged();
                Toast.makeText(TeacherMainActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                Log.d("Teacher", "呼出菜单");
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void MyClassInit()
    {

        deleteMode=false;
        getSupportActionBar().setTitle("我的课堂");
        //TODO 后端->查找老师手下的所有课堂，new TheClass(老师名、课堂名、课堂代码、总学生数)。
        //name 老师名
        //begin
        //样例如下 记得注释
        TheClass temp1 = new TheClass("秦xg","安卓实验","1654321",140);
        list.add(temp1);
        TheClass temp2 = new TheClass("刘sy","java实验","1765413",130);
        list.add(temp2);
        TheClass temp3 = new TheClass("折jz","计网实验","1234564",100);
        list.add(temp3);
        TheClass temp4 = new TheClass("刘sc","javaEE实验","7861231",120);
        list.add(temp4);
        TheClass temp5 = new TheClass("张wh","模拟电路","1651121",140);
        list.add(temp5);
        TheClass temp6 = new TheClass("李yh","java实验","1663513",130);
        list.add(temp6);
        TheClass temp7 = new TheClass("折jz","数据库实验","1184564",120);
        list.add(temp7);

        //begin


        //让布局显示
        myListAdapter=new MyListAdapter(list);
        myListAdapter.setItemClickListener(new MyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                //TODO 前端->进入课堂详情页面
                Toast.makeText(TeacherMainActivity.this, "进入课号"+id, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(TeacherMainActivity.this,TeacherClassActivity.class);
                // Intent intent=new Intent(RegisterActivity.this,StudentMainActivity.class);
                intent.putExtra("classNumber",id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(String id) {
                if(!deleteMode)
                    return;
                //TODO 后端->删除课堂
                //用到的数据
                //id 课堂代码

                //begin
                //end

                Toast.makeText(TeacherMainActivity.this, "删除课号"+id, Toast.LENGTH_SHORT).show();
                for( int i = 0 ; i < list.size() ; i++) {
                    if(list.get(i).getClassNumber().equals(id))
                        list.remove(i);
                }
                myListAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setAdapter(myListAdapter);

    }
    public void addClass(TheClass target )
    {
        list.add(target);
        myListAdapter.notifyDataSetChanged();
    }
}

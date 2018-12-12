package term.rjb.x2l.lessoncheck.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import term.rjb.x2l.lessoncheck.pojo.Lesson;
import term.rjb.x2l.lessoncheck.pojo.MyListAdapter;
import term.rjb.x2l.lessoncheck.pojo.TheClass;
import term.rjb.x2l.lessoncheck.presenter.TeacherPresenter;

public class TeacherMainActivity extends AppCompatActivity implements term.rjb.x2l.lessoncheck.Utils.View {
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
    private List<Lesson> lessons;
    private TeacherPresenter teacherPresenter;
    private Handler handler = new Handler(){
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    for (Lesson lesson : (List<Lesson>)message.obj) {
                        TheClass temp1 = new TheClass(lesson.getTeacher().getName(), lesson.getLessonName(), lesson.getLessonNumber(), lesson.getStudentNum(), lesson.getObjectId());
                        list.add(temp1);
                    }
                    Log.d("测试", "list大小" + list.size());
                    //begin

                    myListAdapter = new MyListAdapter(list);
                    //让布局显示
                    myListAdapter.notifyDataSetChanged();
                    myListAdapter.setItemClickListener(new MyListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String id) {
                            //TODO 前端->进入课堂详情页面
                            Toast.makeText(TeacherMainActivity.this, "进入课号" + id, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TeacherMainActivity.this, TeacherClassActivity.class);
                            // Intent intent=new Intent(RegisterActivity.this,StudentMainActivity.class);
                            intent.putExtra("classNumber", id);
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(String id) {
                            if (!deleteMode)
                                return;

                            Toast.makeText(TeacherMainActivity.this, "删除课号" + id, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getClassNumber().equals(id)) {
                                    String targetObjID = list.get(i).getClassObjID();
                                    Log.d("测试", "删除课号" + id);
                                    teacherPresenter.deleteClass(targetObjID);
                                    //TODO 后端->删除课堂
                                    //用到的数据
                                    //targetObjID OBJID

                                    //begin
                                    //end
                                    list.remove(i);
                                }
                            }
                            myListAdapter.notifyDataSetChanged();
                        }
                    });
                    mRecyclerView.setAdapter(myListAdapter);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
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
        hello.setText(String.format(username + "\n" + "你好 " + name + "老师"));
        setListener();
        MyClassInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                Intent intent = new Intent(TeacherMainActivity.this, CreateClassActivity.class);
                intent.putExtra("name", name);
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

    void MyClassInit() {

        deleteMode = false;
        getSupportActionBar().setTitle("我的课堂");
        //TODO 后端->查找老师手下的所有课堂，new TheClass(老师名、课堂名、课堂代码、总学生数)。
        //username 老师名
        //begin
        //获取该教师所有课堂
        teacherPresenter = new TeacherPresenter(this);
        teacherPresenter.getAllClassByTeacherNumber(handler);
//        while(lessons == null)
////        for(Lesson lesson : lessons){
////            TheClass temp1 = new TheClass(lesson.getTeacher().getName(),lesson.getLessonName(),lesson.getLessonNumber(),lesson.getStudentNum(),lesson.getObjectId());
////            list.add(temp1);
////        }
//
//        //begin
//
//
//        //让布局显示
//        myListAdapter = new MyListAdapter(list);
//        myListAdapter.setItemClickListener(new MyListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(String id) {
//                //TODO 前端->进入课堂详情页面
//                Toast.makeText(TeacherMainActivity.this, "进入课号" + id, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(TeacherMainActivity.this, TeacherClassActivity.class);
//                // Intent intent=new Intent(RegisterActivity.this,StudentMainActivity.class);
//                intent.putExtra("classNumber", id);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(String id) {
//                if (!deleteMode)
//                    return;
//
//                Toast.makeText(TeacherMainActivity.this, "删除课号" + id, Toast.LENGTH_SHORT).show();
//                for (int i = 0; i < list.size(); i++) {
//                    if (list.get(i).getClassNumber().equals(id)) {
//                        String targetObjID = list.get(i).getClassObjID();
//                        //TODO 后端->删除课堂
//                        //用到的数据
//                        //targetObjID OBJID
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

    public void addClass(TheClass target) {
        list.add(target);
        myListAdapter.notifyDataSetChanged();
    }

    @Override
    public void getALLessons(List<Lesson> lessons) {
    }

}


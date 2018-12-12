package term.rjb.x2l.lessoncheck.pojo;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import term.rjb.x2l.lessoncheck.R;



public class MyListAdapter extends RecyclerView.Adapter
        <MyListAdapter.ContactViewHolder> { //MyListAdapter类 开始

    public static interface OnItemClickListener {
        void onItemClick(String id);
        void onItemLongClick(String id);
    }
    private OnItemClickListener mItemClickListener;
    private List<TheClass> contactInfoList;
    public MyListAdapter(List<TheClass> contactInfoList) {
        this.contactInfoList = contactInfoList;
    }
    @Override
    public ContactViewHolder onCreateViewHolder
    (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_class,parent,false);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick((String)view.getTag());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean  onLongClick(View view) {
                mItemClickListener.onItemLongClick((String)view.getTag());
                return true;
            }
        });
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder
            (ContactViewHolder holder, int position) {
        
        TheClass ci = contactInfoList.get(position);
        
        holder.classNumber.setText("课堂代码:" + ci.classNumber);
        holder.classTeacher.setText(ci.teacherName);
        holder.studentNum.setText(ci.studentsNum+"位学员");
        holder.className.setText(ci.className);
       holder.itemView.setTag(ci.classNumber);
    }

    //此方法返回列表项的数目
    @Override
    public int getItemCount() {
        return contactInfoList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        //create the viewHolder class

        protected TextView classNumber;
        protected TextView classTeacher;
        protected TextView studentNum;
        protected TextView className;
        protected RelativeLayout recyclerView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            recyclerView=itemView.findViewById(R.id.layout_RL);
            classNumber = itemView.findViewById(R.id.list_class_Number);
            classTeacher = itemView.findViewById(R.id.list_class_teacher);
            studentNum = itemView.findViewById(R.id.list_class_studentNum);
            className = itemView.findViewById(R.id.list_class_name);
        }

    }
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}

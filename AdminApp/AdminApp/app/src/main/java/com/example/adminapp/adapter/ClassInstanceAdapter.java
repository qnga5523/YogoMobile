package com.example.adminapp.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminapp.R;
import com.example.adminapp.activities.Activity_UpdateClassInstance;
import java.util.ArrayList;
public class ClassInstanceAdapter extends RecyclerView.Adapter<ClassInstanceAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList<String> instance_Id, course_Name, date, teacher, comments;
    private final int layoutResource;
    public ClassInstanceAdapter(Activity activity,Context context,  ArrayList<String>
            instance_Id, ArrayList<String> date, ArrayList<String> teacher,
                                ArrayList<String> comments,
                                ArrayList<String> course_Name, int layoutResource) {
        this.context = context;
        this.activity= activity;
        this.instance_Id = instance_Id;
        this.date = date;
        this.teacher = teacher;
        this.comments = comments;
        this.course_Name = course_Name;
        this.layoutResource = layoutResource;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layoutResource, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("ClassInstanceAdapter", "Displaying item at position: " + position);
        holder.class_id_txt.setText(String.valueOf(position + 1));
        holder.teacherTextView.setText(teacher.get(position));
        holder.classDateTextView.setText(date.get(position));
        holder.course_txt.setText(course_Name.get(position));
        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, Activity_UpdateClassInstance.class);
            intent.putExtra("classInstanceId", Integer.parseInt(instance_Id.get(position)));
            intent.putExtra("classDate", date.get(position));
            intent.putExtra("teacher", teacher.get(position));
            intent.putExtra("comments", comments.get(position));
            intent.putExtra("courseId", course_Name.get(position));
            activity.startActivityForResult(intent, 1);
        });
    }
    @Override
    public int getItemCount() {
        return instance_Id.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView class_id_txt, classDateTextView, teacherTextView, course_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            class_id_txt =itemView.findViewById(R.id.class_id_txt);
            teacherTextView = itemView.findViewById(R.id.teacherTextView);
            classDateTextView = itemView.findViewById(R.id.classDateTextView);
            course_txt = itemView.findViewById(R.id.course_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}

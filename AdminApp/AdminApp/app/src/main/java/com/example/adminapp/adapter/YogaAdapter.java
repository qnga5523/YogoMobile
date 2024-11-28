package com.example.adminapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.activities.Activity_UpdateCourse;

import java.util.ArrayList;

public class YogaAdapter extends RecyclerView.Adapter<YogaAdapter.MyViewHolder> {
    private Context context;
    private Activity activity;
    private ArrayList<String> course_id, course_namecourse, course_description, course_dayOfWeek, course_time, course_type;
    private ArrayList<Integer> course_duration, course_capacity;
    private ArrayList<Double> course_price;
    public YogaAdapter(Activity activity, Context context, ArrayList<String> course_id, ArrayList<String> course_namecourse,
                       ArrayList<String> course_description, ArrayList<String> course_dayOfWeek,
                       ArrayList<String> course_time, ArrayList<Integer> course_duration,
                       ArrayList<Integer> course_capacity, ArrayList<String> course_type,
                       ArrayList<Double> course_price) {
        this.activity = activity;
        this.context = context;
        this.course_id = course_id;
        this.course_namecourse = course_namecourse;
        this.course_description = course_description;
        this.course_dayOfWeek = course_dayOfWeek;
        this.course_time = course_time;
        this.course_duration = course_duration;
        this.course_capacity = course_capacity;
        this.course_type = course_type;
        this.course_price = course_price;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.course_id_txt.setText(String.valueOf(position + 1));
        holder.course_name_txt.setText(course_namecourse.get(position));
        holder.course_dayOfWeek_txt.setText(course_dayOfWeek.get(position));
        holder.course_time_txt.setText(course_time.get(position));
        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, Activity_UpdateCourse.class);
            intent.putExtra("id", course_id.get(position));
            intent.putExtra("namecourse", course_namecourse.get(position));
            intent.putExtra("dayOfWeek", course_dayOfWeek.get(position));
            intent.putExtra("time", course_time.get(position));
            intent.putExtra("capacity", course_capacity.get(position));
            intent.putExtra("duration", course_duration.get(position));
            intent.putExtra("price", course_price.get(position));
            intent.putExtra("type", course_type.get(position));
            intent.putExtra("description", course_description.get(position));
            activity.startActivityForResult(intent, 1);
        });
    }
    @Override
    public int getItemCount() {
        return course_id.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView course_id_txt,course_name_txt, course_dayOfWeek_txt, course_time_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            course_id_txt = itemView.findViewById(R.id.course_id_txt);
            course_name_txt = itemView.findViewById(R.id.course_name_txt);
            course_dayOfWeek_txt = itemView.findViewById(R.id.course_dayOfWeek_txt);
            course_time_txt = itemView.findViewById(R.id.course_time_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}

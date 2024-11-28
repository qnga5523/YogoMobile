package com.example.adminapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.adapter.YogaAdapter;
import com.example.adminapp.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Activity_List_Course extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView no_data;
    DatabaseHelper dbHelper;
    ArrayList<String> course_id, course_namecourse, course_description, course_dayofweek, course_time, course_type;
    ArrayList<Integer> course_duration, course_capacity;
    ArrayList<Double> course_price;
    YogaAdapter yogaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course);
        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_List_Course.this, Activity_AddCourse.class);
                startActivity(intent);
            }
        });
        dbHelper = new DatabaseHelper(Activity_List_Course.this);
        course_id = new ArrayList<>();
        course_namecourse = new ArrayList<>();
        course_dayofweek = new ArrayList<>();
        course_time = new ArrayList<>();
        course_capacity = new ArrayList<>();
        course_duration = new ArrayList<>();
        course_type = new ArrayList<>();
        course_price = new ArrayList<>();
        course_description = new ArrayList<>();
        storeDataInArrays();
        yogaAdapter = new YogaAdapter(Activity_List_Course.this, this, course_id, course_namecourse,
                course_description, course_dayofweek, course_time,
                course_duration, course_capacity, course_type, course_price);
        recyclerView.setAdapter(yogaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Activity_List_Course.this));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
    void storeDataInArrays() {
        Cursor cursor = dbHelper.readYogaMobileData();
        if (cursor.getCount() == 0) {
            no_data.setVisibility(View.GONE);
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                course_id.add(cursor.getString(0));
                course_namecourse.add(cursor.getString(1));
                course_dayofweek.add(cursor.getString(2));
                course_time.add(cursor.getString(3));
                course_capacity.add(cursor.getInt(4));
                course_duration.add(cursor.getInt(5));
                course_price.add(cursor.getDouble(6));
                course_type.add(cursor.getString(7));
                course_description.add(cursor.getString(8));
            }
            no_data.setVisibility(View.GONE);
            Toast.makeText(this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

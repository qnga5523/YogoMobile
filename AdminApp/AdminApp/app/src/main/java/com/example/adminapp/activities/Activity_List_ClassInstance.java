package com.example.adminapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.example.adminapp.adapter.ClassInstanceAdapter;
import com.example.adminapp.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Activity_List_ClassInstance extends AppCompatActivity {
    RecyclerView recyclerView2;
    FloatingActionButton add_class_button;
    TextView no_data;
    DatabaseHelper dbHelper;
    ArrayList<String> instance_Id, date, teacher, comments,course_Name;
    ClassInstanceAdapter classInstanceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_class_instance);
        recyclerView2 = findViewById(R.id.recyclerView2);
        add_class_button = findViewById(R.id.add_class_button);
        no_data = findViewById(R.id.no_data);
        add_class_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_List_ClassInstance.this, Activity_ClassInstance.class);
                startActivity(intent);
            }
        });
        dbHelper = new DatabaseHelper(Activity_List_ClassInstance.this);
        instance_Id = new ArrayList<>();
        date = new ArrayList<>();
        teacher = new ArrayList<>();
        comments = new ArrayList<>();
        course_Name = new ArrayList<>();
        storeDataInArrays();
        classInstanceAdapter = new ClassInstanceAdapter(Activity_List_ClassInstance.this, this,
                instance_Id, date, teacher, comments,course_Name, R.layout.item_class_row);
        recyclerView2.setAdapter(classInstanceAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(Activity_List_ClassInstance.this));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }
    private void storeDataInArrays() {
        Cursor cursor = dbHelper.readClassInstancesData();
        if (cursor.getCount() == 0) {
            no_data.setVisibility(View.GONE);
            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                instance_Id.add(cursor.getString(0));
                date.add(cursor.getString(1));
                teacher.add(cursor.getString(2));
                comments.add(cursor.getString(3));
                course_Name.add(cursor.getString(4));
            }
            no_data.setVisibility(View.GONE);

            Toast.makeText(this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchItem.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(this, Activity_SearchYoga.class);
            startActivity(intent);
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }
}
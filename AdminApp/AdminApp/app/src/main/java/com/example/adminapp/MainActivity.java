package com.example.adminapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adminapp.RetrofitAPI.ApiService;
import com.example.adminapp.RetrofitAPI.CourseUploadData;
import com.example.adminapp.RetrofitAPI.NetworkUtils;
import com.example.adminapp.RetrofitAPI.RetrofitClient;
import com.example.adminapp.activities.Activity_List_ClassInstance;
import com.example.adminapp.activities.Activity_List_Course;
import com.example.adminapp.activities.Activity_SearchYoga;
import com.example.adminapp.database.DataSyncHelper;
import com.example.adminapp.database.DatabaseHelper;
import com.example.adminapp.models.ClassInstance;
import com.example.adminapp.models.YogaCourse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    Button buttonSyncNow, buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        Button buttonCourse = findViewById(R.id.buttonCourse);
        Button buttonClass = findViewById(R.id.buttonClass);
        buttonSyncNow = findViewById(R.id.buttonSyncNow);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_List_Course.class);
                startActivity(intent);
            }
        });
        buttonClass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent= new Intent(MainActivity.this, Activity_List_ClassInstance.class);
                startActivity(intent);
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Activity_SearchYoga.class);
                startActivity(intent);
            }
        });
        buttonSyncNow.setOnClickListener(view -> {
            uploadCoursesAndInstancesToCloud(this);
        });
    }

    public void uploadCoursesAndInstancesToCloud(Context context) {
        DataSyncHelper helper = new DataSyncHelper();
        List<YogaCourse> allCourses = dbHelper.getUnsyncedCourses();
        List<ClassInstance> allInstances = dbHelper.getUnsyncedClassInstances();
        String coursesJson = helper.convertToJson(allCourses);
        String instancesJson = helper.convertInstancesToJson(allInstances);
        Log.d("JSON Data Courses", coursesJson);
        Log.d("JSON Data Instances", instancesJson);
        CourseUploadData data = new CourseUploadData(allCourses, allInstances);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.uploadCoursesWithInstances(data).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    dbHelper.markAsSynced(allCourses, allInstances);
                    Toast.makeText(context, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

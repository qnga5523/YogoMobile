package com.example.adminapp.activities;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.adminapp.R;
import com.example.adminapp.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Activity_UpdateClassInstance extends AppCompatActivity {
      EditText classDateEditText, teacherEditText, commentsEditText;
      Spinner courseSpinner;
      List<Integer> courseIds;
      Button update_class_button, delete_class_button;
      DatabaseHelper dbHelper;
     int classInstanceId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_class_instance);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Update Class Instance");
        }
        dbHelper = new DatabaseHelper(this);
        courseIds = new ArrayList<>();
        initViews();
        loadCoursesIntoSpinner();
        classInstanceId = getIntent().getIntExtra("classInstanceId", -1);
        if (classInstanceId == -1 || !dbHelper.isClassInstanceIdValid(classInstanceId)) {
            Toast.makeText(this, "Invalid class instance ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadClassInstanceDetails(classInstanceId);
        update_class_button.setOnClickListener(view -> updateClassInstance());
        delete_class_button.setOnClickListener(view -> confirmDeleteDialog());
    }
    private void initViews() {
        classDateEditText = findViewById(R.id.classDateEditText);
        teacherEditText = findViewById(R.id.teacherEditText);
        commentsEditText = findViewById(R.id.commentsEditText);
        courseSpinner = findViewById(R.id.courseSpinner);
        update_class_button = findViewById(R.id.update_class_button);
        delete_class_button = findViewById(R.id.delete_class_button);
        classDateEditText.setOnClickListener(v -> showDatePickerDialog());
    }
    private void loadCoursesIntoSpinner() {
        Cursor cursor = dbHelper.readAllCourse();
        List<String> courseNames = new ArrayList<>();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No courses found in the database.", Toast.LENGTH_SHORT).show();
            return;
        }
        while (cursor.moveToNext()) {
            int courseId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow("namecourse"));
            courseIds.add(courseId);
            courseNames.add(courseName);
            Log.d("LoadCourses", "Loaded course: " + courseName + " with ID: " + courseId);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
    }
    private void updateClassInstance() {
        String updatedDate = classDateEditText.getText().toString().trim();
        String updatedTeacher = teacherEditText.getText().toString().trim();
        String updatedComment = commentsEditText.getText().toString().trim();
        int selectedCoursePosition = courseSpinner.getSelectedItemPosition();
        if (selectedCoursePosition == -1) {
            Toast.makeText(this, "Please select a course.", Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedCourseId = courseIds.get(selectedCoursePosition);
        if (updatedDate.isEmpty() || updatedTeacher.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!dbHelper.validateDateMatchesDayOfWeek(selectedCourseId, updatedDate)) {
            Toast.makeText(this, "Date does not match the course's scheduled day of the week.", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean result = dbHelper.updateClassInstance(classInstanceId, updatedDate, updatedTeacher, updatedComment, selectedCourseId);
        if (result) {
            Toast.makeText(this, "Class instance updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update class instance", Toast.LENGTH_SHORT).show();
        }
    }
    private void confirmDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Class Instance?");
        builder.setMessage("Are you sure you want to delete this class instance?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (dbHelper.deleteClassInstance(classInstanceId)) {
                Toast.makeText(this, "Class instance deleted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete class instance", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });
        builder.create().show();
    }
    private void loadClassInstanceDetails(int classInstanceId) {
        Cursor cursor = dbHelper.readClassInstances(classInstanceId);
        if (cursor != null && cursor.moveToFirst()) {
            String classDate = cursor.getString(cursor.getColumnIndexOrThrow("class_date"));
            String teacher = cursor.getString(cursor.getColumnIndexOrThrow("teacher"));
            String comments = cursor.getString(cursor.getColumnIndexOrThrow("comments"));
            int courseId = cursor.getInt(cursor.getColumnIndexOrThrow("course_id"));
            classDateEditText.setText(classDate);
            teacherEditText.setText(teacher);
            commentsEditText.setText(comments);
            selectCourse(courseId);
        } else {
            Toast.makeText(this, "Class instance not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
        cursor.close();
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Activity_UpdateClassInstance.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    classDateEditText.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    private void selectCourse(int courseId) {
        int index = courseIds.indexOf(courseId);
        if (index != -1) {
            courseSpinner.setSelection(index);
        } else {
            Toast.makeText(this, "Course not found.", Toast.LENGTH_SHORT).show();
        }
    }

}

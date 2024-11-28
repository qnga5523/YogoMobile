package com.example.adminapp.activities;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.adminapp.R;
import com.example.adminapp.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Activity_ClassInstance extends AppCompatActivity {
    private EditText classDateEditText, teacherEditText, commentsEditText;
    private Spinner courseSpinner;
    private Button addClassButton;
    private DatabaseHelper dbHelper;
    private List<Integer> courseIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_instance);
        classDateEditText = findViewById(R.id.classDateEditText);
        teacherEditText = findViewById(R.id.teacherEditText);
        commentsEditText = findViewById(R.id.commentsEditText);
        courseSpinner = findViewById(R.id.courseSpinner);
        addClassButton = findViewById(R.id.addClassButton);
        dbHelper = new DatabaseHelper(this);
        courseIds = new ArrayList<>();
        loadCoursesIntoSpinner();
        if(!courseIds.isEmpty()){
            selectCourse(courseIds.get(0));
        };
        classDateEditText.setOnClickListener(v -> showDatePickerDialog());
        addClassButton.setOnClickListener(view -> addClassInstance());
    }
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_ClassInstance.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    classDateEditText.setText(formattedDate);
                }, year, month, day);
        datePickerDialog.show();
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
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
    }
    private void selectCourse(int courseId) {
        int index = courseIds.indexOf(courseId);
        if (index != -1) {
            courseSpinner.setSelection(index);
        }
    }
    private void addClassInstance() {
        String classDate = classDateEditText.getText().toString().trim();
        String teacher = teacherEditText.getText().toString().trim();
        String comments = commentsEditText.getText().toString().trim();
        int selectedCoursePosition = courseSpinner.getSelectedItemPosition();
        int courseId = courseIds.get(selectedCoursePosition);

        if (dbHelper.addClassInstance(classDate, teacher, comments, courseId)) {
            Toast.makeText(this, "Class instance added successfully!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to add class instance.", Toast.LENGTH_SHORT).show();
        }
    }
    private void clearFields() {
        classDateEditText.setText("");
        teacherEditText.setText("");
        commentsEditText.setText("");
        courseSpinner.setSelection(0);
    }
}


package com.example.adminapp.activities;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.adminapp.R;
import com.example.adminapp.adapter.ClassInstanceAdapter;
import com.example.adminapp.database.DatabaseHelper;
import java.util.ArrayList;
public class Activity_UpdateCourse extends AppCompatActivity {
    EditText course_name_txt, description_input, time_input, duration_input, capacity_input, price_input;
    Button update_button, delete_button;
    Spinner spinnerDayOfWeek;
    RadioGroup rgType;
    String id, name, description, dayOfWeek, time, type;
    int duration, capacity;
    double price;
    DatabaseHelper dbHelper;
    RecyclerView instancesRecyclerView;
    ClassInstanceAdapter classInstanceAdapter;
    ArrayList<String> instance_Id, date, teacher, comments, course_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        dbHelper = new DatabaseHelper(this);
        initViews();
        getAndSetIntentData();
        classInstanceAdapter = new ClassInstanceAdapter(this, this, instance_Id, date, teacher, comments, course_Name, R.layout.item_class_course);
        instancesRecyclerView.setAdapter(classInstanceAdapter);
        instancesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadClassInstances();
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(name);
        }
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = course_name_txt.getText().toString().trim();
                String updatedDayOfWeek = spinnerDayOfWeek.getSelectedItem().toString().trim();
                String updatedTime = time_input.getText().toString().trim();
                int updatedCapacity = Integer.parseInt(capacity_input.getText().toString().trim());
                int updatedDuration = Integer.parseInt(duration_input.getText().toString().trim());
                double updatedPrice = Double.parseDouble(price_input.getText().toString().trim());
                int selectedTypeId = rgType.getCheckedRadioButtonId();
                String updatedType = "";
                if (selectedTypeId == R.id.rbFlowYoga) {
                    updatedType = "Flow Yoga";
                } else if (selectedTypeId == R.id.rbAerialYoga) {
                    updatedType = "Aerial Yoga";
                } else if (selectedTypeId == R.id.rbFamilyYoga) {
                    updatedType = "Family Yoga";
                }
                String updatedDescription = description_input.getText().toString().trim();
                boolean result = dbHelper.updateCourse(id, updatedName, updatedDayOfWeek, updatedTime,
                        updatedDuration, updatedCapacity,
                        updatedPrice, updatedType, updatedDescription);
                if (result) {
                    Toast.makeText(Activity_UpdateCourse.this, "Course updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Activity_UpdateCourse.this, "Failed to update course", Toast.LENGTH_SHORT).show();
                }
            }
        });
        delete_button.setOnClickListener(view -> {
            confirmDialog();
        });
    }
    private void loadClassInstances() {
        instance_Id.clear();
        date.clear();
        teacher.clear();
        comments.clear();
        course_Name.clear();

        Cursor cursor = dbHelper.readClassInstancesByCourse(id);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                instance_Id.add(cursor.getString(0));
                date.add(cursor.getString(1));
                teacher.add(cursor.getString(2));
                comments.add(cursor.getString(3));
                course_Name.add(cursor.getString(4));
            }
            cursor.close();
        } else {
            Toast.makeText(this, "No class instances found.", Toast.LENGTH_SHORT).show();
        }
    }
    private void initViews() {
        course_name_txt = findViewById(R.id.course_name_txt2);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek2);
        time_input = findViewById(R.id.time_input2);
        capacity_input = findViewById(R.id.capacity_input2);
        duration_input = findViewById(R.id.duration_input2);
        price_input = findViewById(R.id.price_input2);
        rgType= findViewById(R.id.rgType2);
        description_input = findViewById(R.id.description_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.week_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(adapter);
        time_input.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(Activity_UpdateCourse.this,
                    (view, hourOfDay, minute) -> time_input.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    0, 0, true);
            timePickerDialog.show();
        });
        instancesRecyclerView = findViewById(R.id.instancesRecyclerView);

        instance_Id = new ArrayList<>();
        date = new ArrayList<>();
        teacher = new ArrayList<>();
        comments = new ArrayList<>();
        course_Name = new ArrayList<>();
    }
    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("namecourse")) {
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("namecourse");
            dayOfWeek = getIntent().getStringExtra("dayOfWeek");
            time = getIntent().getStringExtra("time");
            capacity = getIntent().getIntExtra("capacity", 0);
            duration = getIntent().getIntExtra("duration", 0);
            type = getIntent().getStringExtra("type");
            price = getIntent().getDoubleExtra("price", 0.0);
            description = getIntent().getStringExtra("description");
            course_name_txt.setText(name);
            int spinnerPosition = ((ArrayAdapter) spinnerDayOfWeek.getAdapter()).getPosition(dayOfWeek);
            spinnerDayOfWeek.setSelection(spinnerPosition);
            time_input.setText(time);
            capacity_input.setText(String.valueOf(capacity));
            duration_input.setText(String.valueOf(duration));
            price_input.setText(String.valueOf(price));
            description_input.setText(description);
            if (type.equalsIgnoreCase("Flow Yoga")) {
                rgType.check(R.id.rbFlowYoga);
            } else if (type.equalsIgnoreCase("Aerial Yoga")) {
                rgType.check(R.id.rbAerialYoga);
            } else if (type.equalsIgnoreCase("Family Yoga")) {
                rgType.check(R.id.rbFamilyYoga);
            }
        } else {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
            id = "";
        }
    }
    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            String courseId = String.valueOf(id);
            int totalDeleted = dbHelper.deleteCourseAndInstances(courseId);

            if (totalDeleted > 0) {
                Toast.makeText(this, "Deleted " + totalDeleted + " records successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to delete course", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
        });
        builder.create().show();
    }



}

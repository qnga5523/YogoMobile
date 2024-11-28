package com.example.adminapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adminapp.R;
import com.example.adminapp.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Activity_AddCourse extends AppCompatActivity {

    EditText course_name_txt, description_input, duration_input, capacity_input, price_input, time_input;
    Button add_button;
    Spinner spinnerDayOfWeek;
    RadioGroup rgType;
    DatabaseHelper dbHelper;
    FloatingActionButton back_out2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        course_name_txt = findViewById(R.id.course_name_txt);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        time_input = findViewById(R.id.time_input);
        capacity_input = findViewById(R.id.capacity_input);
        duration_input = findViewById(R.id.duration_input);
        price_input = findViewById(R.id.price_input);
        add_button = findViewById(R.id.add_button);
        rgType = findViewById(R.id.rgType);
        description_input = findViewById(R.id.description_input);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.week_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(adapter);
        time_input.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(Activity_AddCourse.this,
                    (view, hourOfDay, minute) -> time_input.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    0, 0, true);
            timePickerDialog.show();
        });
        dbHelper = new DatabaseHelper(Activity_AddCourse.this);
        back_out2 = findViewById(R.id.back_out2);
        back_out2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_button.setOnClickListener(view -> {
            if (course_name_txt.getText().toString().trim().isEmpty() || time_input.getText().toString().trim().isEmpty() || duration_input.getText().toString().trim().isEmpty() ||
                    capacity_input.getText().toString().trim().isEmpty() ||
                    price_input.getText().toString().trim().isEmpty()) {
                Toast.makeText(Activity_AddCourse.this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                String name = course_name_txt.getText().toString().trim();
                String dayOfWeek = spinnerDayOfWeek.getSelectedItem().toString();
                String time = time_input.getText().toString().trim();
                int capacity = Integer.parseInt(capacity_input.getText().toString().trim());
                int duration = Integer.parseInt(duration_input.getText().toString().trim());
                double price = Double.parseDouble(price_input.getText().toString().trim());
                int selectedTypeId = rgType.getCheckedRadioButtonId();
                String type = "";
                if (selectedTypeId == R.id.rbFlowYoga) {
                    type = "Flow Yoga";
                } else if (selectedTypeId == R.id.rbAerialYoga) {
                    type = "Aerial Yoga";
                } else if (selectedTypeId == R.id.rbFamilyYoga) {
                    type = "Family Yoga";
                }
                String description = description_input.getText().toString().trim();
                boolean isAdded = dbHelper.addCourse(name, dayOfWeek, time, capacity, duration, price, type, description);
                if (isAdded) {
                    Toast.makeText(Activity_AddCourse.this, "Course added successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(Activity_AddCourse.this, "Failed to add course. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(Activity_AddCourse.this, "Please enter valid numbers for duration, capacity, and price.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(Activity_AddCourse.this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void clearFields() {
        course_name_txt.setText("");
        spinnerDayOfWeek.setSelection(0);
        time_input.setText("");
        capacity_input.setText("");
        duration_input.setText("");
        price_input.setText("");
        rgType.clearCheck();
        description_input.setText("");
    }
}

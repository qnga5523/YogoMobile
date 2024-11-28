package com.example.adminapp.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.adapter.ClassInstanceAdapter;
import com.example.adminapp.database.DatabaseHelper;

import java.util.ArrayList;

public class Activity_SearchYoga extends AppCompatActivity {
    EditText searchInput;
    Spinner searchCriteriaSpinner;
    Button searchButton;
    RecyclerView searchResultsRecyclerView;
    DatabaseHelper dbHelper;
    ArrayList<String> instanceId, date, teacher, comments, courseName;
    ClassInstanceAdapter classInstanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_yoga);
        dbHelper = new DatabaseHelper(this);
        searchInput = findViewById(R.id.search_input);
        searchCriteriaSpinner = findViewById(R.id.search_criteria_spinner);
        searchButton = findViewById(R.id.search_button);
        searchResultsRecyclerView = findViewById(R.id.search_results_recyclerView);
        instanceId = new ArrayList<>();
        date = new ArrayList<>();
        teacher = new ArrayList<>();
        comments = new ArrayList<>();
        courseName = new ArrayList<>();

        searchButton.setOnClickListener(v -> {
            String searchTerm = searchInput.getText().toString().trim();
            String searchCriteria = searchCriteriaSpinner.getSelectedItem().toString();

            if (searchTerm.isEmpty()) {
                Toast.makeText(Activity_SearchYoga.this, "Please enter a search term",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            performSearch(searchTerm, searchCriteria);
        });
    }
    private void performSearch(String searchTerm, String searchCriteria) {
        Cursor cursor = dbHelper.searchClassInstances(searchTerm, searchCriteria);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No matching classes found.", Toast.LENGTH_SHORT).show();
        } else {
            instanceId.clear();
            date.clear();
            teacher.clear();
            comments.clear();
            courseName.clear();
            while (cursor.moveToNext()) {
                instanceId.add(cursor.getString(0));
                date.add(cursor.getString(1));
                teacher.add(cursor.getString(2));
                comments.add(cursor.getString(3));
                courseName.add(cursor.getString(4));
            }
            cursor.close();
            classInstanceAdapter = new ClassInstanceAdapter(this, this, instanceId,
                    date, teacher, comments, courseName, R.layout.item_class_row);
            searchResultsRecyclerView.setAdapter(classInstanceAdapter);
            searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

}
package com.example.adminapp.database;

import com.example.adminapp.models.ClassInstance;
import com.example.adminapp.models.YogaCourse;
import com.google.gson.Gson;

import java.util.List;

public class DataSyncHelper {
    public String convertToJson(List<YogaCourse> courses) {
        Gson gson = new Gson();
        return gson.toJson(courses);
    }

    public String convertInstancesToJson(List<ClassInstance> instances) {
        Gson gson = new Gson();
        return gson.toJson(instances);
    }
}

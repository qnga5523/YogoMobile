package com.example.adminapp.RetrofitAPI;

import com.example.adminapp.models.ClassInstance;
import com.example.adminapp.models.YogaCourse;

import java.util.List;

public class CourseUploadData {
    private List<YogaCourse> courses;
    private List<ClassInstance> classInstances;

    public CourseUploadData(List<YogaCourse> courses, List<ClassInstance> classInstances) {
        this.courses = courses;
        this.classInstances = classInstances;
    }
    public List<YogaCourse> getCourses() {
        return courses;
    }
    public void setCourses(List<YogaCourse> courses) {
        this.courses = courses;
    }
    public List<ClassInstance> getClassInstances() {
        return classInstances;
    }
    public void setClassInstances(List<ClassInstance> classInstances) {
        this.classInstances = classInstances;
    }
}

package com.example.adminapp.models;

public class ClassInstance {
    private int instanceId;
    private int courseId;
    private String date;
    private String teacher;
    private String comments;
    private boolean isSynced;
    public boolean isSynced() {
        return isSynced;
    }
    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    public int getInstanceId() {
        return instanceId;
    }
    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }
    public int getCourseId() {
        return courseId;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}

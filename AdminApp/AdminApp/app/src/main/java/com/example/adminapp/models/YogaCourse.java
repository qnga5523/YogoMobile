package com.example.adminapp.models;

public class YogaCourse {
    private int id;
    private String name;
    private String dayOfWeek;
    private String time;
    private int capacity;
    private int duration;
    private double price;
    private String type;
    private String description;
    private boolean isSynced;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public boolean isSynced() {
        return isSynced;
    }
}

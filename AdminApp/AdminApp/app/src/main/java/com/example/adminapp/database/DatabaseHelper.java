package com.example.adminapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.adminapp.models.ClassInstance;
import com.example.adminapp.models.YogaCourse;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "yoga.db";
    private static final int DATABASE_VERSION = 4;
//Course
    private static final String TABLE_YOGA_MOBILE = "yoga_mobile";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAMECOURSE = "namecourse";
    private static final String COLUMN_DAY_OF_WEEK = "dayOfWeek";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_CAPACITY = "capacity";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IS_SYNCED = "is_synced";

//Class Instance
private static final String TABLE_CLASS_INSTANCES = "class_instances";
    private static final String COLUMN_CLASS_INSTANCE_ID = "_id";
    private static final String COLUMN_CLASS_DATE = "class_date";
    private static final String COLUMN_TEACHER = "teacher";
    private static final String COLUMN_COMMENTS = "comments";
    private static final String COLUMN_COURSE_ID = "course_id";


    private static final String CREATE_TABLE_YOGA_MOBILE = "CREATE TABLE " + TABLE_YOGA_MOBILE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAMECOURSE + " TEXT, " +
            COLUMN_DAY_OF_WEEK + " TEXT, " +
            COLUMN_TIME + " TEXT, " +
            COLUMN_CAPACITY + " INTEGER, " +
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_PRICE + " REAL, " +
            COLUMN_TYPE + " TEXT, " +
            COLUMN_DESCRIPTION + " TEXT, " +
            COLUMN_IS_SYNCED + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_CLASS_INSTANCES = "CREATE TABLE " + TABLE_CLASS_INSTANCES + " (" +
            COLUMN_CLASS_INSTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CLASS_DATE + " TEXT NOT NULL, " +
            COLUMN_TEACHER + " TEXT NOT NULL, " +
            COLUMN_COMMENTS + " TEXT, " +
            COLUMN_COURSE_ID + " INTEGER NOT NULL, " +
            COLUMN_IS_SYNCED + " INTEGER DEFAULT 0, " +
            "FOREIGN KEY (" + COLUMN_COURSE_ID + ") REFERENCES " + TABLE_YOGA_MOBILE + "(" + COLUMN_ID + ")" +
            ")";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_YOGA_MOBILE);
        db.execSQL(CREATE_TABLE_CLASS_INSTANCES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_YOGA_MOBILE + " ADD COLUMN " + COLUMN_IS_SYNCED + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_CLASS_INSTANCES + " ADD COLUMN " + COLUMN_IS_SYNCED + " INTEGER DEFAULT 0");
            Log.d("DatabaseHelper", "Database upgraded to add is_synced column.");
        }
    }
    public boolean addCourse(String namecourse, String dayOfWeek, String time,
                             int capacity, int duration, double price, String type, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAMECOURSE, namecourse);
        cv.put(COLUMN_DAY_OF_WEEK, dayOfWeek);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_CAPACITY, capacity);
        cv.put(COLUMN_DURATION, duration);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_DESCRIPTION, description);
        try {
            long result = db.insert(TABLE_YOGA_MOBILE, null, cv);
            if (result == -1) {
                Log.e("DatabaseHelper", "Failed to insert row into " + TABLE_YOGA_MOBILE);
                return false;
            } else {
                Log.d("DatabaseHelper", "Course added successfully with ID: " + result);
                return true;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while inserting data: " + e.getMessage());
            return false;
        }
    }
    public boolean updateCourse(String id, String namecourse, String dayOfWeek, String time, int capacity,
                                int duration, double price, String type, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAMECOURSE, namecourse);
        cv.put(COLUMN_DAY_OF_WEEK, dayOfWeek);
        cv.put(COLUMN_TIME, time);
        cv.put(COLUMN_CAPACITY, capacity);
        cv.put(COLUMN_DURATION, duration);
        cv.put(COLUMN_PRICE, price);
        cv.put(COLUMN_TYPE, type);
        cv.put(COLUMN_DESCRIPTION, description);
        long result = db.update(TABLE_YOGA_MOBILE, cv, "_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public int deleteCourseAndInstances(String courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        int totalDeleted = 0;
        try {
            int classInstancesDeleted = db.delete(TABLE_CLASS_INSTANCES, COLUMN_COURSE_ID + "=?", new String[]{courseId});
            Log.d("DatabaseHelper", "Class instances deleted: " + classInstancesDeleted);
            totalDeleted += classInstancesDeleted;
            int coursesDeleted = db.delete(TABLE_YOGA_MOBILE, COLUMN_ID + "=?", new String[]{courseId});
            Log.d("DatabaseHelper", "Courses deleted: " + coursesDeleted);
            totalDeleted += coursesDeleted;
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error during delete transaction", e);
        } finally {
            db.endTransaction();
        }
        return totalDeleted;
    }



    public boolean deleteClassInstance(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CLASS_INSTANCES, "_id=?", new String[]{String.valueOf(id)});
        return result != -1;
    }
    public Cursor readYogaMobileData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_YOGA_MOBILE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            Log.d("DatabaseHelper", "No data found in TABLE_YOGA_MOBILE.");
        } else {
            Log.d("DatabaseHelper", "Data found in TABLE_YOGA_MOBILE: " + cursor.getCount() + " records.");
        }
        return cursor;
    }
    public Cursor readClassInstancesData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                TABLE_CLASS_INSTANCES + "." + COLUMN_CLASS_INSTANCE_ID + ", " +
                TABLE_CLASS_INSTANCES + "." + COLUMN_CLASS_DATE + ", " +
                TABLE_CLASS_INSTANCES + "." + COLUMN_TEACHER + ", " +
                TABLE_CLASS_INSTANCES + "." + COLUMN_COMMENTS + ", " +
                TABLE_YOGA_MOBILE + "." + COLUMN_NAMECOURSE +
                " FROM " + TABLE_CLASS_INSTANCES +
                " LEFT JOIN " + TABLE_YOGA_MOBILE +
                " ON " + TABLE_CLASS_INSTANCES + "." + COLUMN_COURSE_ID + " = " + TABLE_YOGA_MOBILE + "." + COLUMN_ID;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            Log.d("DatabaseHelper", "No data found in TABLE_CLASS_INSTANCES.");
        } else {
            Log.d("DatabaseHelper", "Data found in TABLE_CLASS_INSTANCES: " + cursor.getCount() + " records.");
        }
        return cursor;
    }
    public boolean validateDateMatchesDayOfWeek(int courseId, String classDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_DAY_OF_WEEK + " FROM " + TABLE_YOGA_MOBILE + " WHERE " + COLUMN_ID + "=?",
                new String[]{String.valueOf(courseId)}
        );
        if (cursor.moveToFirst()) {
            String expectedDayOfWeek = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_OF_WEEK));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date date = dateFormat.parse(classDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                String actualDayOfWeek = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                if (!expectedDayOfWeek.equalsIgnoreCase(actualDayOfWeek)) {
                    Toast.makeText(context, "Selected date does not match the course's scheduled day of the week.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(context, "Invalid date format.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Log.e("DatabaseHelper", "No matching course ID found.");
        }
        cursor.close();
        return true;
    }


    public boolean addClassInstance(String classDate, String teacher, String comments, int courseId) {
        if (!validateDateMatchesDayOfWeek(courseId, classDate)) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CLASS_DATE, classDate);
        cv.put(COLUMN_TEACHER, teacher);
        cv.put(COLUMN_COMMENTS, comments);
        cv.put(COLUMN_COURSE_ID, courseId);

        long result = db.insert(TABLE_CLASS_INSTANCES, null, cv);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert class instance");
            return false;
        } else {
            Log.d("DatabaseHelper", "Class instance added successfully with ID: " + result);
            return true;
        }
    }
    public boolean updateClassInstance(int classInstanceId, String classDate, String teacher, String comments, int courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CLASS_DATE, classDate);
        cv.put(COLUMN_TEACHER, teacher);
        cv.put(COLUMN_COMMENTS, comments);
        cv.put(COLUMN_COURSE_ID, courseId);
        int result = db.update(TABLE_CLASS_INSTANCES, cv, "_id=?", new String[]{String.valueOf(classInstanceId)});
        return result != -1;
    }
    public Cursor readClassInstancesByCourse(String courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ci._id, ci.class_date, ci.teacher, ci.comments, ym.namecourse " +
                "FROM class_instances ci " +
                "JOIN yoga_mobile ym ON ci.course_id = ym._id " +
                "WHERE ci.course_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{courseId});
        if (cursor.getCount() == 0) {
            Log.d("DatabaseHelper", "No instances found for courseId: " + courseId);
        } else {
            Log.d("DatabaseHelper", "Instances found: " + cursor.getCount());
        }
        return cursor;
    }
    public Cursor readClassInstances(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM class_instances WHERE _id=?", new String[]{String.valueOf(id)});
    }
    public Cursor readAllCourse() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT _id, namecourse FROM yoga_mobile", null);
    }
    public boolean isClassInstanceIdValid(int classInstanceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM class_instances WHERE _id = ?", new String[]{String.valueOf(classInstanceId)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public Cursor searchClassInstances(String query, String criteria) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlQuery;
        switch (criteria) {
            case "teacher":
                sqlQuery = "SELECT ci._id, ci.class_date, ci.teacher, ci.comments, ym.namecourse " +
                        "FROM class_instances ci " +
                        "JOIN yoga_mobile ym ON ci.course_id = ym._id " +
                        "WHERE ci.teacher LIKE ?";
                query = "%" + query + "%";
                break;
            case "date":
                sqlQuery = "SELECT ci._id, ci.class_date, ci.teacher, ci.comments, ym.namecourse " +
                        "FROM class_instances ci " +
                        "JOIN yoga_mobile ym ON ci.course_id = ym._id " +
                        "WHERE ci.class_date = ?";
                break;
            case "dayOfWeek":
                sqlQuery = "SELECT ci._id, ci.class_date, ci.teacher, ci.comments, ym.namecourse " +
                        "FROM class_instances ci " +
                        "JOIN yoga_mobile ym ON ci.course_id = ym._id " +
                        "WHERE ym.dayOfWeek = ?";
                break;
            default:
                return null;
        }
        return db.rawQuery(sqlQuery, new String[]{query});
    }



    public List<YogaCourse> getUnsyncedCourses() {
        List<YogaCourse> courses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM yoga_mobile  WHERE is_synced = 0", null);
        int isSyncedIndex = cursor.getColumnIndex("is_synced");
        if (isSyncedIndex == -1) {
            cursor.close();
            return courses;
        }
        if (cursor.moveToFirst()) {
            do {
                YogaCourse course = new YogaCourse();
                course.setId(cursor.getInt(0));
                course.setName(cursor.getString(1));
                course.setDayOfWeek(cursor.getString(2));
                course.setTime(cursor.getString(3));
                course.setCapacity(cursor.getInt(4));
                course.setDuration(cursor.getInt(5));
                course.setPrice(cursor.getDouble(6));
                course.setType(cursor.getString(7));
                course.setDescription(cursor.getString(8));
                course.setSynced(cursor.getInt(isSyncedIndex) == 1);
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courses;
    }
    public List<ClassInstance> getUnsyncedClassInstances() {
        List<ClassInstance> instances = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM class_instances WHERE is_synced = 0", null);
        int isSyncedIndex = cursor.getColumnIndex("is_synced");
        if (isSyncedIndex == -1) {
            cursor.close();
            return instances;
        }
        if (cursor.moveToFirst()) {
            do {
                ClassInstance instance = new ClassInstance();
                instance.setInstanceId(cursor.getInt(0));
                instance.setDate(cursor.getString(1));
                instance.setTeacher(cursor.getString(2));
                instance.setComments(cursor.getString(3));
                instance.setCourseId(cursor.getInt(4));
                instance.setSynced(cursor.getInt(isSyncedIndex) == 1);
                instances.add(instance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return instances;
    }
    public void markAsSynced(List<YogaCourse> courses, List<ClassInstance> instances) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (YogaCourse course : courses) {
                ContentValues values = new ContentValues();
                values.put("is_synced", 1);
                db.update("yoga_mobile", values, "_id = ?", new String[]{String.valueOf(course.getId())});
            }
            for (ClassInstance instance : instances) {
                ContentValues values = new ContentValues();
                values.put("is_synced", 1);
                db.update("class_instances", values, "_id = ?", new String[]{String.valueOf(instance.getInstanceId())});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}


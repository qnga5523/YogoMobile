package com.example.adminapp.RetrofitAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @POST("uploadCoursesWithInstances")
    Call<Void> uploadCoursesWithInstances(@Body CourseUploadData data);

}

package com.example.jeroe.inhollandnewsreader_student550395.Services;

import com.example.jeroe.inhollandnewsreader_student550395.AuthToken;
import com.example.jeroe.inhollandnewsreader_student550395.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("users/login")
    Call<AuthToken> login(@Body User user);
}
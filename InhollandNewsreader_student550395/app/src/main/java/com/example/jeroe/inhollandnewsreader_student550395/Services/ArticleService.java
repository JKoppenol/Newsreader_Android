package com.example.jeroe.inhollandnewsreader_student550395.Services;

import com.example.jeroe.inhollandnewsreader_student550395.News;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {
    @GET("articles")
    Call<News> articles(
            @Header("x-authtoken") String authtoken,
            @Query("count") Integer count);

    @GET("articles")
    Call<News> articles(
            @Query("count") Integer count);

    @GET("articles/{id}")
    Call<News> article(
            @Header("x-authtoken") String authtoken,
            @Path("id") Integer articleId,
            @Query("count") Integer count);

    @GET("articles/{id}")
    Call<News> article(
            @Path("id") Integer articleId,
            @Query("count") Integer count);

    @PUT("articles/{id}/like")
    Call<Void> putLikeArticle(
            @Header("x-authtoken") String authtoken,
            @Path("id") Integer articleId);

    @DELETE("articles/{id}/like")
    Call<Void> deleteLikeArticle(
            @Header("x-authtoken") String authtoken,
            @Path("id") Integer articleId);
}
package com.anugrahrochmat.chuck.rest;

import com.anugrahrochmat.chuck.model.RandomCategory;
import com.anugrahrochmat.chuck.model.RandomJoke;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("random")
    Call<RandomJoke> getRandomJoke();

    @GET("random")
    Call<RandomCategory> getRandomCategory(@Query("category") String category);

    @GET("categories")
    Call<ArrayList<String>> getCategoryList();

}

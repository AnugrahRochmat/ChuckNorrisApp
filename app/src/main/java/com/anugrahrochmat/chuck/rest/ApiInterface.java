package com.anugrahrochmat.chuck.rest;

import com.anugrahrochmat.chuck.model.Result;
import com.anugrahrochmat.chuck.model.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("random")
    Call<Result> getRandomCategory(@Query("category") String category);

    @GET("categories")
    Call<List<String>> getCategoryList();

    @GET("search")
    Call<SearchResult> getSearchQuery(@Query("query") String query);
}

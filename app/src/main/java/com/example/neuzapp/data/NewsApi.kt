package com.example.neuzapp.data

import com.example.neuzapp.data.remote.dto.NewsDto
import com.example.neuzapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    // https://newsapi.org/v2/top-headlines?country=in&apiKey=abb08159438e44b2a6a3817fc5e8856f
    // baseUrl = https://newsapi.org/v2/
    // endpoint = top-headlines
    // ? = parameters { seperated by & } and can provide more parameters [a/c to api doc.]
    // firstParameter = country
    // secondParameter = apiKey

    @GET("top-headlines")
    suspend fun getTopNewsHeadlines(
        @Query("category") category: String,
        @Query("country") country: String = "in",
        @Query("apiKey") apiKey: String = Constants.API_KEY
    ): NewsDto
}
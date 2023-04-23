package com.example.kcodetest.datasource.remote.service

import com.example.kcodetest.datasource.remote.model.ITunesSearchResponse
import retrofit2.http.GET

interface ITunesApiService {
    @GET("/search?term=jack+johnson&entity=album")
    suspend fun getSearchResponse(): ITunesSearchResponse
}
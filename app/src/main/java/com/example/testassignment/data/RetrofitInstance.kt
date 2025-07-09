package com.example.testassignment.data

import com.example.testassignment.network.FrameService
import com.example.testassignment.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val frameService: FrameService by lazy {
        retrofit.create(FrameService::class.java)
    }
}
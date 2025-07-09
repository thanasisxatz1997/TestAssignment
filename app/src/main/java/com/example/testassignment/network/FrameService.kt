package com.example.testassignment.network

import com.example.testassignment.models.ImageFrame
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface FrameService {
    @POST("streamFrame")
    fun streamFrame(@Body imageFrame: ImageFrame): Call<Void>
}
package com.example.testassignment.data.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.testassignment.data.RetrofitInstance
import com.example.testassignment.models.ImageFrame
import com.example.testassignment.network.FrameService
import com.example.testassignment.utils.Constants
import com.example.testassignment.utils.StatsStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FrameRepository {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: FrameService = retrofit.create(FrameService::class.java)

    fun streamFrame(context: Context, imageFrame: ImageFrame) {
        if (Constants.isNetworkAvailable(context)) {
            val call = service.streamFrame(imageFrame)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    StatsStorage.incrementFrameCount(context)
                    StatsStorage.addBytesSent(context, imageFrame.base64Image.toByteArray().size)
                    if (response.isSuccessful) {
                        Log.i("Response", "Frame streamed successfully.")
                    } else {
                        when (response.code()) {
                            400 -> Log.e("Error 400", "Bad Request")
                            404 -> Log.e("Error 404", "Not Found")
                            else -> Log.e("Error", "Unexpected Error: ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    StatsStorage.incrementFrameCount(context)
                    StatsStorage.addBytesSent(context, imageFrame.base64Image.toByteArray().size)
                    Log.e("Network Error", "Streaming failed: ${t.localizedMessage}")
                }
            })
        } else {
            Toast.makeText(context, "No internet connection.", Toast.LENGTH_SHORT).show()
        }
    }
}
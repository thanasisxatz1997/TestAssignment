package com.example.testassignment.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants{
    const val BASE_URL:String="http://testApi/"
    fun isNetworkAvailable(context:Context):Boolean{
        val connectivityManager=context.
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            val network=connectivityManager.activeNetwork?:return false
            val activeNetwork=connectivityManager.getNetworkCapabilities(network)?:return false
            return when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true //gia lan
                else->false
            }

        }
        else
        {
            //Old and deprecated. versions < 23
            val networkInfo=connectivityManager.activeNetworkInfo
            return networkInfo!=null && networkInfo.isConnectedOrConnecting
        }
    }
}
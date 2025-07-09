package com.example.testassignment.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testassignment.utils.StatsStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val _frameCount = MutableLiveData<Int>()
    private val _bytesSent = MutableLiveData<Long>()

    val frameCount: LiveData<Int> get() = _frameCount
    val bytesSent: LiveData<Long> get() = _bytesSent

    init {
        loadStats()
    }

    fun loadStats() {
        val context = getApplication<Application>().applicationContext
        //coroutine gia apofugh lag
        viewModelScope.launch(Dispatchers.IO) {
            val frames = StatsStorage.getFrameCount(context)
            val bytes = StatsStorage.getBytesSent(context)
            withContext(Dispatchers.Main) {
                _frameCount.value = frames
                _bytesSent.value = bytes
            }
        }
    }

    fun resetStats() {
        val context = getApplication<Application>().applicationContext
        StatsStorage.resetStats(context)
        loadStats()
    }
}
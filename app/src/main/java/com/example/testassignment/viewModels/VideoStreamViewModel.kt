package com.example.testassignment.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.testassignment.data.repositories.FrameRepository
import com.example.testassignment.models.ImageFrame

class VideoStreamViewModel(application: Application) : AndroidViewModel(application) {

    fun sendFrame(imageFrame: ImageFrame) {
        FrameRepository.streamFrame(getApplication(), imageFrame)
    }
}
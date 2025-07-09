package com.example.testassignment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.testassignment.databinding.ActivityVideoStreamBinding
import com.example.testassignment.utils.FrameProcessor
import com.example.testassignment.viewModels.VideoStreamViewModel

class VideoStreamActivity : AppCompatActivity() {
    private lateinit var viewModel: VideoStreamViewModel
    private var binding: ActivityVideoStreamBinding?=null
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var analyzingFrames = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = ViewModelProvider(this)[VideoStreamViewModel::class.java]
        binding= ActivityVideoStreamBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding?.btnSwitchCamera?.setOnClickListener {
            lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                CameraSelector.LENS_FACING_FRONT
            } else {
                CameraSelector.LENS_FACING_BACK
            }
            startCamera()
        }

        binding?.btnStreamStart?.setOnClickListener {
            analyzingFrames = true
            binding?.btnStreamStart?.visibility = View.GONE
            binding?.btnStreamStartEnd?.visibility = View.VISIBLE
        }

        binding?.btnStreamStartEnd?.setOnClickListener {
            analyzingFrames = false
            binding?.btnStreamStart?.visibility = View.VISIBLE
            binding?.btnStreamStartEnd?.visibility = View.GONE
        }

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding?.previewView?.surfaceProvider)
            }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                if (analyzingFrames) {
                    FrameProcessor.handleImageFrame(imageProxy) { frame ->
                        viewModel.sendFrame(frame)
                    }
                } else {
                    imageProxy.close()
                }
            }

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(lensFacing)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
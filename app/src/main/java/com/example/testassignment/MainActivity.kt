package com.example.testassignment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.testassignment.databinding.ActivityMainBinding
import android.Manifest
import android.app.Activity
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    companion object{
        private const val CAMERA_PERMISSION_CODE=1 //permission
        private const val TAG="CameraX"
    }
    private enum class NavigationTarget {
        IMAGE_CAPTURE,
        VIDEO_STREAM
    }
    private var pendingNavigationTarget: NavigationTarget? = null
    private var imageCapture: ImageCapture? = null
    private var binding:ActivityMainBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //val flCameraButton:FrameLayout=findViewById(R.id.flCamera)
        //flCameraButton.setOnClickListener{...}
        binding?.flImage?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
            {
                val intent=Intent(this,ImageCaptureActivity::class.java)
                startActivity(intent)
            }
            else
            {
                pendingNavigationTarget = NavigationTarget.IMAGE_CAPTURE
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }

        binding?.flStreamVideo?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED)
            {
                val intent=Intent(this,VideoStreamActivity::class.java)
                startActivity(intent)
            }
            else
            {
                pendingNavigationTarget = NavigationTarget.VIDEO_STREAM
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }



        binding?.flStats?.setOnClickListener {
            val intent=Intent(this,StatsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== CAMERA_PERMISSION_CODE)
        {
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                when (pendingNavigationTarget) {
                    NavigationTarget.IMAGE_CAPTURE -> {
                        startActivity(Intent(this, ImageCaptureActivity::class.java))
                    }
                    NavigationTarget.VIDEO_STREAM -> {
                        startActivity(Intent(this, VideoStreamActivity::class.java))
                    }
                    null -> {
                        Toast.makeText(this, "Permission granted but no action specified", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else
            {
                Toast.makeText(this@MainActivity,"Camera permission denied.",Toast.LENGTH_SHORT).show()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode== Activity.RESULT_OK)
//        {
//            if(requestCode == CAMERA_REQUEST_CODE)
//            {
//                Toast.makeText(this@MainActivity,"Image Taken.",Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}
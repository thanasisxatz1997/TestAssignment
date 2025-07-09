package com.example.testassignment.utils
import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.example.testassignment.models.ImageFrame

object FrameProcessor {
    @SuppressLint("UnsafeOptInUsageError")
    fun handleImageFrame(
        imageProxy: ImageProxy,
        onFrameReady: (ImageFrame) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val bitmap: Bitmap = ImageUtils.imageProxyToBitmap(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val base64 = ImageUtils.bitmapToBase64(bitmap)

            val imageFrame = ImageFrame(
                base64Image = base64,
                timestamp = System.currentTimeMillis()
            )

            onFrameReady(imageFrame)
        }
        imageProxy.close()
    }
}
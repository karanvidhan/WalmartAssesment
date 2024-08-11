package com.example.myapplication.cache

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

// Function to download and save image locally
fun downloadAndCacheImage(context: Context, imageUrl: String, imageView: ImageView, cacheDir: String) {
    // Generate a unique file name based on the URL
    val fileName = imageUrl.hashCode().toString()
    val file = File(context.cacheDir, "$cacheDir/$fileName.jpg")

    // Check if the image is already cached
    if (file.exists()) {
        // Load the image from the cache
        Glide.with(context)
            .load(file)
            .apply(RequestOptions().centerCrop())
            .into(imageView)
    } else {
        // Download the image and save it locally
        Thread {
            try {
                val url = URL(imageUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

                // Save the bitmap to local storage
                file.parentFile?.mkdirs() // Create directory if it doesn't exist
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // Load the image into ImageView on the main thread
                (context as? android.app.Activity)?.runOnUiThread {
                    Glide.with(context)
                        .load(file)
                        .apply(RequestOptions().centerCrop())
                        .into(imageView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}

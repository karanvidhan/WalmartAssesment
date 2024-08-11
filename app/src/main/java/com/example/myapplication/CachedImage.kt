package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun CachedImage(url: String) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(url) {
        bitmap = loadBitmapFromCacheOrNetwork(context, url)
    }
    bitmap?.let {
        Image(bitmap = it.asImageBitmap(), contentDescription = null)
    }
}

suspend fun loadBitmapFromCacheOrNetwork(context: Context, imageUrl: String): Bitmap? {
    val fileName = imageUrl.hashCode().toString()
    val file = File(context.cacheDir, fileName)
    
    return if (file.exists()) {
        BitmapFactory.decodeFile(file.absolutePath)
    } else {
        val bitmap = downloadBitmap(imageUrl)
        bitmap?.let {
            saveBitmapToFile(it, file)
        }
        bitmap
    }
}

suspend fun downloadBitmap(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("ImageDownload", "Failed to download image", e)
            null
        }
    }
}

suspend fun saveBitmapToFile(bitmap: Bitmap, file: File) {
    withContext(Dispatchers.IO) {
        try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: Exception) {
            Log.e("ImageSave", "Failed to save image", e)
        }
    }
}

@Preview
@Composable
fun PreviewCachedImage() {
    CachedImage(url = "https://example.com/sample.jpg")
}

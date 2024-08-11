package com.example.myapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.room.Room
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.cache.CachedImage
import com.example.myapplication.data.remote.ApiClient
import com.example.myapplication.data.local.ApodDatabase
import com.example.myapplication.repository.ApodRepository
import com.example.myapplication.viewModel.ApodViewModel
import com.example.myapplication.viewModel.ApodViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: ApodViewModel by viewModels {
        val db = Room.databaseBuilder(
            applicationContext,
            ApodDatabase::class.java, "apod-database"
        ).build()
        val repository = ApodRepository(ApiClient.retrofitService, db.apodDao(), applicationContext)
        ApodViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchApod("P1LLPatcPoFX7vhyi5pPM6jPjaZIyzdTD5147o3B")
                ApodScreen(viewModel)

        }
    }
}

@Composable
fun ApodScreen(viewModel: ApodViewModel) {
    val apod by viewModel.apod.observeAsState()
    apod?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (it.media_type == "image") {
                ImageContent(url = it.url)   //for Image Media Type
            } else {
                WebViewContent(url = it.url)  // for Video Media Type
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it.title, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it.explanation, style = MaterialTheme.typography.body1)

        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading...", style = MaterialTheme.typography.h5)
        }
    }
}

@Composable
fun MyImageScreen() {
    CachedImage(url = "")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Column {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "App Icon", // Provide a meaningful description
            modifier = Modifier.height(100.dp) // Adjust the size as needed
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Astronomy Picture of the Day")
    }
}

@Composable
fun WebViewContent(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Adjust as needed
    )
}

@Composable
fun ImageContent(url: String) {
    if (isNetworkAvailable()) {
        Image(
            painter = rememberAsyncImagePainter(model = url),
            contentDescription = "Media Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
    }else{
        Text(
            text = "We are unable to show the image as network is not there. Please wait for next update to check image offline", style = MaterialTheme.typography.body1,
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .height(250.dp)
        )
    }
}

@Composable
private fun isNetworkAvailable(): Boolean {
    val connectivityManager = LocalContext.current.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo
        @Suppress("DEPRECATION")
        return networkInfo != null && networkInfo.isConnected
    }
}


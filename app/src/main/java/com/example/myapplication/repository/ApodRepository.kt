package com.example.myapplication.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.example.myapplication.data.local.APODEntity
import com.example.myapplication.data.local.ApodDao
import com.example.myapplication.data.remote.NasaApiService
import java.text.SimpleDateFormat
import java.util.*

class ApodRepository(private val apiService: NasaApiService, private val apodDao: ApodDao, private val context: Context) {

    suspend fun getApod(apiKey: String): APODEntity? {
        val currentDate = getCurrentDate()
        val cachedApod = apodDao.getApodByDate(currentDate)

        return if (isNetworkAvailable(context)) {
            val response = apiService.getAPOD(apiKey)
            if (response.isSuccessful) {
                response.body()?.let {
                    apodDao.insert(it)
                    it
                }
            } else {
                cachedApod
            }
        } else {
            if (cachedApod==null){
                Toast.makeText(context, "No internet connection. Please check your network and try again.", Toast.LENGTH_LONG).show()
            }
            cachedApod
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}

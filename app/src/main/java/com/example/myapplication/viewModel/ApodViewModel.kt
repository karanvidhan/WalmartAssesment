package com.example.myapplication.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.APODEntity
import com.example.myapplication.repository.ApodRepository
import kotlinx.coroutines.launch

class ApodViewModel(private val repository: ApodRepository) : ViewModel() {

    private val _apod = MutableLiveData<APODEntity?>()
    val apod: LiveData<APODEntity?> = _apod

    fun fetchApod(apiKey: String) {
        viewModelScope.launch {
            val apodData = repository.getApod(apiKey)
            _apod.postValue(apodData)
        }
    }
}

package com.peter.music.ui.main

import android.app.Application
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.peter.music.MyApplication
import com.peter.music.pojo.Track
import com.peter.music.service.SearchSource
import com.peter.music.service.TracksUseCase
import com.peter.music.service.repository.TracksRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(private val usecase : TracksUseCase) :
    ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _data = MutableLiveData<List<Track>>()
    val data: LiveData<List<Track>>
        get() = _data

    val query = MutableLiveData<String>()

    init {
        onSearch()
    }

      fun onSearch() {
          Log.i("ViewModel","created")

          viewModelScope.launch {
            usecase.getTracks("asd")
        }
    }
}
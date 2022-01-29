package com.peter.music.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.music.ApiStatus
import com.peter.music.pojo.Track
import com.peter.music.service.TracksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.net.HttpRetryException
import javax.inject.Inject
import kotlinx.coroutines.flow.asFlow

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(private val usecase: TracksUseCase) :
    ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    private val _data = MutableLiveData<List<Track>>()
    val data: LiveData<List<Track>>
        get() = _data

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    private val _selectedItem = MutableLiveData<Track>()
    val selectedItem: LiveData<Track>
        get() = _selectedItem


    fun onSearch(query: String) {
        if (query.length > 2) {
            _status.value = ApiStatus.LOADING
            _data.value = ArrayList()
            coroutineScope.launch {
                usecase.getTracks(query, _data, _status)
            }
        } else _data.value = ArrayList()
    }

    fun displayPropertyDetails(property: Track) {
        _selectedItem.value = property
    }

    fun displayPropertyDetailsComplete() {
        _selectedItem.value = null
    }
}
package com.peter.music.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.music.ApiStatus
import com.peter.music.pojo.Track
import com.peter.music.service.business.usecase.TracksUseCase
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
        if (query.length > 2)
            viewModelScope.launch {
                _data.value = ArrayList()
                usecase.getTracks(query)
                    .onStart {
                        _status.value = ApiStatus.LOADING
                    }
                    .retryWhen { cause, attempt ->
                        return@retryWhen cause is HttpRetryException && attempt < 3
                    }
                    .catch {
                        _status.value = ApiStatus.ERROR
                    }
                    .collect {
                        _data.value = it
                        if(it.isEmpty())
                        _status.value = ApiStatus.EMPTY
                        else _status.value  = ApiStatus.DONE
                    }

            }
    }

    fun displayPropertyDetails(property: Track) {
        _selectedItem.value = property
    }

    fun displayPropertyDetailsComplete() {
        _selectedItem.value = null
    }
}
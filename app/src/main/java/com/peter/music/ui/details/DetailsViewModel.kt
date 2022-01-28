package com.peter.music.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.peter.music.pojo.Track

class DetailsViewModel(@Suppress("UNUSED_PARAMETER") track: Track, app: Application) :
    AndroidViewModel(app) {

    private val _selectedProperty = MutableLiveData<Track>()
    val selectedProperty: LiveData<Track>
        get() = _selectedProperty

    init {
        _selectedProperty.value = track
    }
}
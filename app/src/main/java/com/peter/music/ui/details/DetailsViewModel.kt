package com.peter.music.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.music.pojo.Track

class DetailsViewModel() :
    ViewModel() {

    private val _selectedProperty = MutableLiveData<Track>()
    val selectedProperty: LiveData<Track>
        get() = _selectedProperty

    fun setProperty(track: Track) {
     _selectedProperty.value = track
    }

}
package com.peter.music.ui.details

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peter.music.pojo.Track

class DetailsViewModelFactory(
    private val track: Track,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(track, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

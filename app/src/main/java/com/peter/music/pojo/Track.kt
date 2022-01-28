package com.peter.music.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    var image: String,
    var id: String,
    var title: String,
    var type: String,
    var mainArtist: String,
    var publishingDate: String,
) : Parcelable

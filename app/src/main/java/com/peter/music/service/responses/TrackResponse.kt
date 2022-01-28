package com.peter.music.service.responses

import com.google.gson.annotations.SerializedName
import com.peter.music.pojo.MainArtist
import com.peter.music.pojo.Track
import kotlin.random.Random

data class TrackResponse(
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("genres")
    val genres: List<String>?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("mainArtist")
    val mainArtist: MainArtist?,
    @SerializedName("publishingDate")
    val publishingDate: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("cover")
    val cover: Cover?,

)



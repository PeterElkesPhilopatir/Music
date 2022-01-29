package com.peter.music.service.business.responses

import com.google.gson.annotations.SerializedName

data class Authentication(
    @SerializedName("accessToken")
    val accessToken: String?,
    @SerializedName("expiresIn")
    val expiresIn: String?,
    @SerializedName("tokenType")
    val tokenType: String = "Bearer"

)
package com.peter.music

object Constant {
    const val TOKEN = "v0/api/gateway/token/client"
    const val SEARCH = "v2/api/sayt/flat"
    const val QUERY = "query"
    const val PREF_NAME = "music_data_pref"
    const val ACCESSTOKEN = "accessToken"

}

object HeadersValue {
    const val BEARER = "Bearer"
    const val APP_JSON = "application/json"
    const val APPLICATION_X_WWWW_FORM = "application/x-www-form-urlencoded"
}

object HeadersKey {
    const val GATEWAY_KEY = "X-MM-GATEWAY-KEY"
    const val AUTHORIZATION = "Authorization"
    const val ACCEPT = "Accept"
    const val CONTENT_TYPE = "Content-Type"
}

object PrefKeys {
    const val PREF_TOKEN = "token"

}

enum class ApiStatus { LOADING, DONE,ERROR,EMPTY }



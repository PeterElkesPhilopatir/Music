package com.peter.music.service

import android.content.Context
import android.util.Log
import com.peter.music.Constant.PREF_NAME
import com.peter.music.PrefKeys.PREF_TOKEN

import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
interface TokenSource {

    fun saveToken(token: String)
    fun getToken(): String

}class TokenSourceImpl @Inject constructor(
    @ApplicationContext context: Context
) : TokenSource {
    private val sharedPref = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )

    override fun saveToken(token: String) {
        Log.i("TokenSource", token)
        with(sharedPref.edit())
        {
            putString(PREF_TOKEN, token)
            apply()
        }

    }

    override fun getToken() = sharedPref.getString(PREF_TOKEN, "")!!
}
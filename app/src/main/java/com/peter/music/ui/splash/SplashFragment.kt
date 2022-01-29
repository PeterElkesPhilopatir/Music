package com.peter.music.ui.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.peter.music.R
import kotlinx.coroutines.*


class SplashFragment : Fragment() {

    private val scope = CoroutineScope(Dispatchers.Main)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        scope.launch {
            delay(2000)
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onPause() {
        scope.cancel()
        super.onPause()
    }

}
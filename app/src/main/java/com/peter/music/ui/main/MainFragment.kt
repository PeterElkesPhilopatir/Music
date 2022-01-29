package com.peter.music.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.peter.music.ApiStatus
import com.peter.music.databinding.FragmentMainBinding
import com.peter.music.service.TracksUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.apply {
            recyclerView.adapter = TracksAdapter(OnClickListener {
                viewModel!!.displayPropertyDetails(it)
            })

            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel!!.onSearch(query)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return true
                }
            })
        }


        viewModel.apply {
            selectedItem.observe(viewLifecycleOwner) {
                if (null != it) {
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToDetailsFragment(
                            it
                        )
                    )
                    viewModel.displayPropertyDetailsComplete()
                }
            }
            status.observe(viewLifecycleOwner) {
                if (it == ApiStatus.ERROR)
                    Toast.makeText(context, "API Error", Toast.LENGTH_SHORT).show()
                if (it == ApiStatus.EMPTY)
                    Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root


    }
}
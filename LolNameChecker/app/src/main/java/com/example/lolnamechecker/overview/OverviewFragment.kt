package com.example.lolnamechecker.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.lolnamechecker.databinding.FragmentOverviewBinding
import com.example.lolnamechecker.R

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOverviewBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.searchButton.setOnClickListener { searchForPlayer() }


        return binding.root
    }

    // calls the function getNumberTimes to see the number of times a player has played with another player
    private fun searchForPlayer() {
        val summonerName: String = binding.summonerNameEditText.text.toString()
        val searchName: String = binding.checkedSummonerNameEditText.text.toString()
        if (summonerName.isNullOrEmpty() || searchName.isNullOrEmpty()) {
            displayErrorToast(R.string.empty_input_error.toString())
        } else {
            viewModel.getNumberTimes(summonerName, searchName)
        }
    }

    // create and display a toast with the inputted error message
    // TODO: figure out how to use this in the viewModel when the app errors
    private fun displayErrorToast(errorMsg: String) {
        val toast = Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG)
        toast.show()
    }

}
package com.example.lolnamechecker

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.lolnamechecker.overview.RiotApiStatus

// set a the image view based on the current status of the program
@BindingAdapter("riotApiStatus")
fun bindStatus(statusImageView: ImageView, status: RiotApiStatus?) {
    when (status) {
        RiotApiStatus.SEARCHING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        RiotApiStatus.ERROR -> {
            statusImageView.visibility = View.INVISIBLE
        }
        RiotApiStatus.DONE -> {
            statusImageView.visibility = View.INVISIBLE
        }
    }
}
package com.peter.music

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peter.music.service.image_load.ImageLoader
import com.peter.music.pojo.Track
import com.peter.music.ui.main.TracksAdapter


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    try {
            imgView.clipToOutline = true
        val imgLoader = ImageLoader(imgView.context)

        imgLoader.DisplayImage(imgUrl!!, R.drawable.loading_animation, imgView)

    } catch (e: Exception) {
        Log.e("BindingAdapter",e.message.toString())
        imgView.setImageResource(R.mipmap.ic_launcher)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Track>?) {
    val adapter = recyclerView.adapter as TracksAdapter
    adapter.submitList(data)
}

@BindingAdapter("shareClicked")
fun bindShare(shareTextView: TextView, item: Track) {
    shareTextView.setOnClickListener {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "See this Track " + item.title + "\n" + "From Here " + item.title
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)

        try {
            shareTextView.context.startActivity(shareIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                shareTextView.context,
                shareTextView.context.getString(R.string.sharing_not_available),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
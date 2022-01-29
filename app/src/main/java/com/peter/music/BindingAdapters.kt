package com.peter.music

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

import com.peter.music.pojo.Track
import com.peter.music.ui.main.TracksAdapter
import java.net.URL
import kotlin.concurrent.thread


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    try {
        imgView.clipToOutline = true
        if (imgUrl != null) {

            val uiHandler = Handler(Looper.getMainLooper())
            thread(start = true) {
                val bitmap = downloadBitmap(imgUrl)
                uiHandler.post {
                    imgView.setImageBitmap(bitmap)
                }
            }

//            try {
//                CoroutineScope(Dispatchers.IO).launch {
//                    imgView.setImageResource(R.drawable.loading_animation)
//                    val bitmap = downloadBitmap(imgUrl)
//                    withContext(Dispatchers.Main) {
//                        imgView.setImageBitmap(bitmap)
//                    }
//                }
//            } catch (e: Exception) {
//                imgView.setImageResource(R.drawable.ic_broken_image)
//            }
        } else imgView.setImageResource(R.drawable.ic_broken_image)

    } catch (e: Exception) {
        Log.e("BindingAdapter", e.message.toString())
        imgView.setImageResource(R.drawable.ic_broken_image)
    }
}

private fun downloadBitmap(imageUrl: String): Bitmap? {
    return try {
        val conn = URL(imageUrl).openConnection()
        conn.connect()
        val inputStream = conn.getInputStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap
    } catch (e: Exception) {
        null
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

@BindingAdapter("apiStatus")
fun bindStatus(bar: ProgressBar, status: ApiStatus?) {
    if (status == ApiStatus.LOADING)
        bar.visibility = View.VISIBLE
    else bar.visibility = View.GONE


}
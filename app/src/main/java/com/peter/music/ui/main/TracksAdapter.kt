package com.peter.music.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peter.music.databinding.TrackItemBinding
import com.peter.music.pojo.Track

class TracksAdapter(val onClickListener: OnClickListener) :
    ListAdapter<Track, TrackViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TrackViewHolder {
        return TrackViewHolder(TrackItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

class TrackViewHolder(private var binding: TrackItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Track) {
        binding.data = item
        binding.txtShare.setOnClickListener {
        }
        binding.executePendingBindings()
    }
}

class OnClickListener(val clickListener: (item: Track) -> Unit) {
    fun onClick(item: Track) = clickListener(item)
}
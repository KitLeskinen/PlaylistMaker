package com.practicum.playlistmaker.presentation.model.ui.search

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Tools
import com.practicum.playlistmaker.databinding.SearchViewBinding
import com.practicum.playlistmaker.domain.entity.Track

import java.util.Locale

class SearchViewHolder(private val binding: SearchViewBinding) : RecyclerView.ViewHolder(binding.root) {

//    private val trackCover: ImageView = itemView.findViewById(R.id.trackCover)
//    private val trackName: TextView = itemView.findViewById(R.id.trackName)
//    private val artistName: TextView = itemView.findViewById(R.id.artistName)
//    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: Track){
        Glide.with(itemView).load(model.artworkUrl100).placeholder(R.drawable.placeholder).transform(RoundedCorners(
            Tools.dpToPx(2f, itemView.context)
        )).into(binding.trackCover)
        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
    }
}
package com.practicum.playlistmaker.medialibrary.ui

import android.icu.text.SimpleDateFormat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Tools
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.SearchViewBinding
import java.util.Locale

class FavoritesViewHolder(private val binding: SearchViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Track){
        Glide.with(itemView).load(model.artworkUrl100).placeholder(R.drawable.placeholder).transform(RoundedCorners(
            Tools.dpToPx(2f, itemView.context)
        )).into(binding.trackCover)
        binding.trackName.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
    }
}
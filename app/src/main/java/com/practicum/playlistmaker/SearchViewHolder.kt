package com.practicum.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackCover: ImageView = itemView.findViewById(R.id.trackCover)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind(model: Track){
        Glide.with(itemView).load(model.artworkUrl100).placeholder(R.drawable.placeholder).transform(RoundedCorners(Tools.dpToPx(2f, itemView.context))).into(trackCover)
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)
    }
}
package com.practicum.playlistmaker.audio_player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Tools
import com.practicum.playlistmaker.common.data.domain.entity.Playlist

class BottomSheetPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val name: TextView = itemView.findViewById(R.id.playlistName)
    private val tracksCount: TextView = itemView.findViewById(R.id.tracksCount)
    private val imageView: ImageView = itemView.findViewById(R.id.trackCover)

    fun bind(playlist: Playlist) {
        name.text = playlist.name
        tracksCount.text = "${playlist.trackList?.size} ${playlist.trackList?.let {
            Tools.declensions(context = tracksCount.context,
                it.size)
        }}"

        Glide.with(imageView).load(playlist.coverUri)
            .placeholder(R.drawable.placeholder).transform(
                CenterCrop(),
                RoundedCorners(Tools.dpToPx(8f, imageView.context))
            ).into(imageView)
    }
}
package com.practicum.playlistmaker.audio_player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.data.domain.entity.Playlist

class BottomSheetPlaylistAdapter(private val playlists: List<Playlist>) : RecyclerView.Adapter<BottomSheetPlaylistViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_playlist_view_item, parent, false)
        return BottomSheetPlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }


}
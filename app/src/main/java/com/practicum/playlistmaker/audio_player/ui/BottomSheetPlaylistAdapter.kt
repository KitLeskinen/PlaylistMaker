package com.practicum.playlistmaker.audio_player.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.data.domain.entity.Playlist


class BottomSheetPlaylistAdapter(private val playlists: List<Playlist>,     private val onPressAction: OnPressAction) : RecyclerView.Adapter<BottomSheetPlaylistViewHolder>(){

    fun interface OnPressAction {
        fun invoke(playlists: Playlist)
    }

    private val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetPlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bottomsheet_playlist_view_item, parent, false)
        return BottomSheetPlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    private var isClickAllowed: Boolean = true

    private fun clickDebounce() : Boolean{
        val current = isClickAllowed
        if(isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY )

        }
        return current
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener() {
            if(clickDebounce()){
                onPressAction.invoke(playlists[position])
            }
        }
    }


}
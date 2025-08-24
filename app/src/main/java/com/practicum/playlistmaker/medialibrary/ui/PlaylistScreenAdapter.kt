package com.practicum.playlistmaker.medialibrary.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.SearchViewBinding


class PlaylistScreenAdapter(
    private var tracks: List<Track>,
    private val onClick: OnClick,
    private val onLongClick: OnLongClick
) : RecyclerView.Adapter<FavoritesViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    fun interface OnClick {
        fun invoke(track: Track)
    }

    fun interface OnLongClick {
        fun invoke(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return FavoritesViewHolder(SearchViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private var isClickAllowed: Boolean = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)

        }
        return current
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener() {
            if (clickDebounce()) {
                onClick.invoke(tracks[position])
            }
        }
        holder.itemView.setOnLongClickListener() {
            onLongClick.invoke(tracks[position])
            true
        }
    }


}


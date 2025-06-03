package com.practicum.playlistmaker.medialibrary.ui

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.SearchViewBinding


class FavoritesAdapter(
    private var tracks: List<Track>,
    private val addToHistory: AddToHistory
) : RecyclerView.Adapter<FavoritesViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    fun interface AddToHistory {
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

    private fun clickDebounce() : Boolean{
        val current = isClickAllowed
        if(isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY )

        }
        return current
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener() {
            if(clickDebounce()){
                addToHistory.invoke(tracks[position])
            }
        }
    }



}


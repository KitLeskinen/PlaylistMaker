package com.practicum.playlistmaker

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    private var tracks: List<Track>,
    private val addToHistory: SearchAdapter.AddToHistory
) : RecyclerView.Adapter<SearchViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    fun updateList(list: List<Track>) {
        tracks = list.reversed()
    }

    fun interface AddToHistory {
        fun invoke(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_view, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private var isClickAllowed: Boolean = true

    private fun clickDebounce() : Boolean{
        var current = isClickAllowed
        if(isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY )

        }
        return current
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener() {
            if(clickDebounce()){
                addToHistory.invoke(tracks[position])
            }
        }
    }

}


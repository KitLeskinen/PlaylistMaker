package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(
    private val tracks: List<Track>,
    private val addToHistory: SearchAdapter.AddToHistory
) : RecyclerView.Adapter<SearchViewHolder>() {


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

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener() {
          addToHistory.invoke(tracks[position])
        }
    }

}


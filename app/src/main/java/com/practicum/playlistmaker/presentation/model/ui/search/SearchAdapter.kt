package com.practicum.playlistmaker.presentation.model.ui.search

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.SearchViewBinding
import com.practicum.playlistmaker.domain.entity.Track


class SearchAdapter(
    private var tracks: List<Track>,
    private val addToHistory: AddToHistory
) : RecyclerView.Adapter<SearchViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())

    fun updateList(list: List<Track>) {
        tracks = list.reversed()
        notifyDataSetChanged()
    }

    fun interface AddToHistory {
        fun invoke(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_view, parent, false)
//        return SearchViewHolder(view)
        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchViewHolder(SearchViewBinding.inflate(layoutInspector, parent, false))
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


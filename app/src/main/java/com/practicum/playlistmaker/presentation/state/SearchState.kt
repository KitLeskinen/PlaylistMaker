package com.practicum.playlistmaker.presentation.state

import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.presentation.model.ui.search.SearchViewModel

sealed interface SearchState {
    object Loading: SearchState
    object Searching: SearchState
    data class TrackAddedToHistory(val history: List<Track> ): SearchState
    data class Error(val message: String) : SearchState
    object ResultEmpty: SearchState
    object Result: SearchState
}
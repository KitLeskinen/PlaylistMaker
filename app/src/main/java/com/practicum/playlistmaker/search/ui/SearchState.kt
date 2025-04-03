package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.common.data.domain.entity.Track

sealed interface SearchState {
    data class Loading(val history: List<Track>): SearchState
    data class TextFieldClicked(val fieldIsEmpty: Boolean, val historyIsEmpty: Boolean, val history: List<Track>): SearchState
    object Searching: SearchState
    data class Result(val list: List<Track>): SearchState
    data class Error(val message: String): SearchState
    class HistoryCleared : SearchState
    data class FieldCleared(val history: List<Track>): SearchState
}
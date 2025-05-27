package com.practicum.playlistmaker.search.ui

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse
import com.practicum.playlistmaker.search.domain.HistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    private val historyInteractor: HistoryInteractor,
    private val trackInteractor: TracksInteractor
) : ViewModel() {

    val TAG = "DEBUG"

    private var searchJob: Job? = null
    var history = historyInteractor.getTracksHistory().reversed().toMutableList()

    private var lastSearch = ""

    private val searchState = MutableLiveData<SearchState>()

    init {
        searchState.value = SearchState.Loading(history)
    }

    fun getSearchState(): LiveData<SearchState> {
        return searchState
    }

    fun addTrackToHistory(track: Track) {
        history = historyInteractor.getTracksHistory()
        val trackIndex = history.indexOf(track)
        if (trackIndex != -1) history.removeAt(trackIndex)

        history.add(track)

        if (history.size > 10) history.removeAt(0)

        Log.d("addTrackToHistory", "History List")
        history.forEachIndexed { index, trackItem ->
            Log.d(
                TAG,
                "addTrackToHistory: $index: ${trackItem.trackName} - ${trackItem.artistName}"
            )
        }
    }


    fun saveTracksHistory() {
        historyInteractor.saveTracksHistory(history)
        Log.d(TAG, "onCreate: ${historyInteractor.getTracksHistory()}")

    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        history.clear()
        searchState.value = SearchState.HistoryCleared()
    }

    fun makeResponse(text: String) {

        searchState.value = SearchState.Searching
        viewModelScope.launch {
            trackInteractor
                .getTracks(text)
                .collect { trackResponse ->
                    processResult(trackResponse)
                }
        }
    }

    private fun processResult(trackResponse: TrackResponse) {
        if(trackResponse.isError){
            searchState.value = SearchState.Error(trackResponse.errorMessage)
        } else{
            searchState.value = SearchState.Result(trackResponse.trackList)
        }
    }


    fun searchDebounce(query: String) {
        if (query.isEmpty()) {
            searchJob?.cancel()
            return
        }
        lastSearch = query
        Log.d(TAG, "searchDebounce: ")
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            makeResponse(lastSearch)
        }

    }


    fun searchEditTextClicked(text: Editable) {
        Log.d(TAG, "searchEditTextClicked: ")
        if (text.isEmpty()) {
            searchJob?.cancel()
        }
        searchState.value = SearchState.TextFieldClicked(text.isEmpty(), history.isEmpty(), history)
    }

    fun clearField() {
        searchJob?.cancel()

        searchState.value = SearchState.FieldCleared(history)
    }

    fun deleteResponse() {
        Log.d(TAG, "deleteResponse: ")
        searchJob?.cancel()
        searchState.value = SearchState.TextFieldClicked(true, history.isEmpty(), history)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


}
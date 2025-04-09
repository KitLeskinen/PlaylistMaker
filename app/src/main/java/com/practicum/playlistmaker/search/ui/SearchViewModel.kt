package com.practicum.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.common.data.domain.api.ConsumerData
import com.practicum.playlistmaker.common.data.domain.entity.Track

import com.practicum.playlistmaker.common.data.domain.entity.TrackResponse
import com.practicum.playlistmaker.search.domain.HistoryInteractor
import org.koin.core.context.GlobalContext.get
import org.koin.java.KoinJavaComponent.inject


class SearchViewModel(private val historyInteractor: HistoryInteractor) : ViewModel() {

    val TAG = "DEBUG"
    private val trackInteractor = Creator.provideTrackInteractor()

//    private val historyInteractor: HistoryInteractor by inject()

    private val handler = Handler(Looper.getMainLooper())
    private var detailsRunnable: Runnable? = null

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

        trackInteractor.getTracks(text, object :
            com.practicum.playlistmaker.common.data.domain.api.Consumer<TrackResponse> {
            override fun consume(data: ConsumerData<TrackResponse>) {

                val currentRunnable = detailsRunnable
                if (currentRunnable != null) {
                    handler.removeCallbacks(currentRunnable)
                }


                val newDetailsRunnable = Runnable {
                    when (data) {
                        is ConsumerData.Data -> {
                            searchState.value = SearchState.Result(data.value.trackList)
                            Log.d(TAG, data.value.trackList.toString())

                        }

                        is ConsumerData.Error -> {
                            searchState.value = SearchState.Error(data.message)
                            Log.d(TAG, "consume error: ${data}")
//                            searchState.value = SearchState.Error(data.message)
                            Log.d(TAG, data.message)

                        }
                    }
                }
                detailsRunnable = newDetailsRunnable
                handler.post(newDetailsRunnable)
            }
        })
    }

    val searchRunnable = Runnable {
        Log.d(TAG, "new Runnable: ")
        searchState.value = SearchState.Searching
        makeResponse(lastSearch)
    }

    fun searchDebounce(query: String) {
        if (query.isEmpty()) {
            handler.removeCallbacks(searchRunnable)
            return
        }
            lastSearch = query
            Log.d(TAG, "searchDebounce: ")
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)

    }


    fun searchEditTextClicked(text: Editable) {
        Log.d(TAG, "searchEditTextClicked: ")
        if (text.isEmpty()) {
            handler.removeCallbacks(searchRunnable)
        }
        searchState.value = SearchState.TextFieldClicked(text.isEmpty(), history.isEmpty(), history)
    }

    fun clearField() {
        handler.removeCallbacks(searchRunnable)
        searchState.value = SearchState.FieldCleared(history)
    }

    fun deleteResponse() {
        Log.d(TAG, "deleteResponse: ")
        handler.removeCallbacks(searchRunnable)
        searchState.value = SearchState.TextFieldClicked(true, history.isEmpty(), history)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

//        fun factory(
//        ): ViewModelProvider.Factory {
//            return viewModelFactory {
//                initializer {
//                    SearchViewModel()
//                }
//            }
//        }
    }


}
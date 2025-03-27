package com.practicum.playlistmaker.presentation.model.ui.search

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.domain.api.Consumer
import com.practicum.playlistmaker.domain.api.ConsumerData
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.entity.TrackResponse
import com.practicum.playlistmaker.presentation.state.AdapterState
import com.practicum.playlistmaker.presentation.state.HistoryState
import com.practicum.playlistmaker.presentation.state.SearchState

class SearchViewModel(private val application: Application) : ViewModel() {

    private val trackInteractor = Creator.provideTrackInteractor()

    private var historyInteractor  = Creator.provideHistoryInteractor(application)

    private val handler = Handler(Looper.getMainLooper())
    private var detailsRunnable: Runnable? = null

    private val tracksList = MutableLiveData<List<Track>>()

    fun getTracks(): MutableLiveData<List<Track>>{
        return tracksList
    }

    var history = historyInteractor.getTracksHistory().reversed().toMutableList()

    private var lastSearch = ""

    private var adapterState = MutableLiveData<AdapterState>()

    private var historyIsEmpty: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun getHistoryIsEmpty(): MutableLiveData<Boolean>{
        return historyIsEmpty
    }

    private val historyState = MutableLiveData<HistoryState>()
    init {
        adapterState.value = AdapterState.Loading(history)
    }
    fun getHistoryState(): MutableLiveData<HistoryState>{
        return historyState
    }

    private val searchState= MutableLiveData<SearchState>()

    fun getSearchState() : MutableLiveData<SearchState>{
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
                "HISTORY",
                "addTrackToHistory: $index: ${trackItem.trackName} - ${trackItem.artistName}"

            )
        }
    }





    fun saveTracksHistory(){
        historyInteractor.saveTracksHistory(history)
        Log.d("HISTORY", "onCreate: ${historyInteractor.getTracksHistory()}")

    }

    fun clear(){
        history.clear()
    }

    fun clearHistory(){
        historyInteractor.clearHistory()
    }

    fun getAdapterState(): MutableLiveData<AdapterState>{
        return adapterState
    }

    fun onClickHistoryButton(){
        adapterState.value = AdapterState.AdapterUpdated(history)
    }

    fun makeResponse(text: String) {

        searchState.value = SearchState.Responsing




        trackInteractor.getTracks(text, object : Consumer<TrackResponse> {
            override fun consume(data: ConsumerData<TrackResponse>) {

                val currentRunnable = detailsRunnable
                if (currentRunnable != null) {
                    handler.removeCallbacks(currentRunnable)
                }


                val newDetailsRunnable = Runnable {
                    when (data) {
                        is ConsumerData.Data -> {
                            tracksList.value = data.value.trackList
                            if (data.value.trackList.isEmpty()) {
                                searchState.value = SearchState.ResultEmpty
                            } else {
                                searchState.value = SearchState.Result
                            }


                            Log.d("TRACKS", data.value.trackList.toString())

                        }

                        is ConsumerData.Error -> {
                            Log.d("CONSUME", "consume error: ${data}")
                            searchState.value = SearchState.Error(data.message)
                            Log.d("RESPONSE", data.message)

                        }
                    }
                }
                detailsRunnable = newDetailsRunnable
                handler.post(newDetailsRunnable)
            }
        })
    }

    fun setLastSearch(text: String){
        lastSearch = text
    }

    val searchRunnable = Runnable {
        adapterState.value = AdapterState.AdapterSearch

    }

    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun updateButtonClicked(){
        makeResponse(lastSearch)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun factory(
            application: Application
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel(application)
                }
            }
        }
    }


}
package com.practicum.playlistmaker.presentation.model.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

import com.practicum.playlistmaker.Creator

import com.practicum.playlistmaker.R


import com.practicum.playlistmaker.domain.api.Consumer
import com.practicum.playlistmaker.domain.api.ConsumerData
import com.practicum.playlistmaker.domain.api.HistoryInteractor
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.entity.TrackResponse


import com.practicum.playlistmaker.presentation.model.ui.audio_player.AudioPlayerActivity

const val EXTRA_SELECTED_TRACK = "EXTRA_SELECTED_TRACK"

class SearchActivity : AppCompatActivity() {

    private val trackInteractor = Creator.provideTrackInteractor()

    private lateinit var historyInteractor: HistoryInteractor

    private var searchQuery = ""
    private lateinit var searchEditText: EditText


    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }

    private val handler = Handler(Looper.getMainLooper())
    private var detailsRunnable: Runnable? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
        Log.d("Search", "onSaveInstanceState searchQuery: $searchQuery")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY).toString()
        searchEditText.setText(searchQuery)
        Log.d("Search", "onRestoreInstanceState searchQuery: $searchQuery")

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        historyInteractor = Creator.provideHistoryInteractor(this)

        //region initialize Views
        val backImageView = findViewById<MaterialToolbar>(R.id.back)
        searchEditText = findViewById(R.id.searchEditText)
        val clearImageView = findViewById<ImageView>(R.id.clear)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val errorIcon = findViewById<ImageView>(R.id.errorIcon)
        val errorMessage = findViewById<TextView>(R.id.errorMessage)
        val updateButton = findViewById<MaterialButton>(R.id.updateButton)

        val tracksList = mutableListOf<Track>()
        val clearHistoryButton = findViewById<MaterialButton>(R.id.clearHistoryButton)
        val headerHistory = findViewById<TextView>(R.id.headerHistory)
        //endregion
//        val searchHistorySharedPreferences =

        var history =  historyInteractor.getTracksHistory().reversed().toMutableList()

        val searchRecyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)

        var lastSearch = ""


        val historySearchAdapter = SearchAdapter(history) { track ->
            showAudioPlayerActivity(track)
        }

        fun setErrorVisibility(isVisible: Boolean) {
            if (isVisible) {
                errorIcon.isVisible = true
                errorMessage.isVisible = true
            } else {
                errorIcon.visibility = View.GONE
                errorMessage.visibility = View.GONE
            }

        }


        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && history.isNotEmpty()) {
                headerHistory.isVisible = true
                clearHistoryButton.isVisible = true
                searchRecyclerView.adapter = historySearchAdapter
            } else {
                clearHistoryButton.isVisible = false
            }
        }

        //region History
        fun addTrackToHistory(track: Track) {
            history = historyInteractor.getTracksHistory()
            val trackIndex = history.indexOf(track)
            if (trackIndex != -1) history.removeAt(trackIndex)

            history.add(track)

            if (history.size > 10) history.removeAt(0)
            historySearchAdapter.updateList(history)
            historySearchAdapter.notifyDataSetChanged()
            Log.d("addTrackToHistory", "History List")
            history.forEachIndexed { index, trackItem ->
                Log.d(
                    "HISTORY",
                    "addTrackToHistory: $index: ${trackItem.trackName} - ${trackItem.artistName}"

                )
            }
        }

        //endregion


        val searchAdapter = SearchAdapter(tracksList) { track ->
            showAudioPlayerActivity(track)


            addTrackToHistory(track)
            // add file to shared preferences
            historyInteractor.saveTracksHistory(history)
            Log.d("HISTORY", "onCreate: ${historyInteractor.getTracksHistory()}")
            Log.d("HISTORY", "Track added to history: ${track.trackName}")
            Toast.makeText(
                this,
                "${track.trackName} - ${track.artistName} добавлен",
                Toast.LENGTH_SHORT
            ).show()
        }



        clearHistoryButton.setOnClickListener() { _ ->
            history.clear()
            historyInteractor.clearHistory()
            historySearchAdapter.updateList(history)
            historySearchAdapter.notifyDataSetChanged()
            headerHistory.isVisible = false
            clearHistoryButton.isVisible = false

        }

        // set blank or restored text in search field
        searchEditText.setText(searchQuery)
        Log.d("Search", "onCreate searchQuery: $searchQuery")

        // clear search field
        clearImageView.setOnClickListener() { view ->
            searchEditText.setText("")
            tracksList.clear()
            setErrorVisibility(false)
            searchRecyclerView.isVisible = history.isNotEmpty()
            searchAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // close SearchActivity
        backImageView.setNavigationOnClickListener {
            finish()
        }

        fun makeResponse(text: String) {
            progressBar.isVisible = true

            fun extracted() {
                errorIcon.setImageResource(R.drawable.no_search_results)
                errorMessage.text = getString(R.string.no_results)
                setErrorVisibility(true)
            }


            trackInteractor.getTracks(text, object : Consumer<TrackResponse> {
                override fun consume(data: ConsumerData<TrackResponse>) {

                    val currentRunnable = detailsRunnable
                    if (currentRunnable != null) {
                        handler.removeCallbacks(currentRunnable)
                    }


                    val newDetailsRunnable = Runnable {
                        when (data) {
                            is ConsumerData.Data -> {
                                progressBar.visibility = View.GONE
                                if (data.value.trackList.isEmpty()) {
                                    extracted()
                                } else {
                                    setErrorVisibility(false)
                                }
                                tracksList.clear()
                                tracksList.addAll(data.value.trackList)
                                Log.d("TRACKS", data.value.trackList.toString())
                                searchAdapter.notifyDataSetChanged()
                                searchRecyclerView.isVisible = true
                            }

                            is ConsumerData.Error -> {
                                Log.d("CONSUME", "consume error: ${data}")
                                progressBar.visibility = View.GONE
                                Log.d("RESPONSE", data.message)
                                searchRecyclerView.visibility = View.GONE
                                errorIcon.setImageResource(R.drawable.no_internet)
                                errorMessage.text = getString(R.string.no_internet_check_connection)
                                setErrorVisibility(true)
                                updateButton.isVisible = true
                                lastSearch = searchEditText.text.toString()
                            }
                        }
                    }
                    detailsRunnable = newDetailsRunnable
                    handler.post(newDetailsRunnable)
                }
            })
        }

        val searchRunnable = Runnable {
            searchRecyclerView.adapter = searchAdapter
            makeResponse(searchEditText.text.toString())
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        // Show or hide clear button for text search
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nothing to do
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // button is invisible if search text field empty


                if (p0.isNullOrEmpty()) {

                    clearImageView.isVisible = false
                    updateButton.isVisible = false
                    if (history.isNotEmpty()) {
                        clearHistoryButton.isVisible = true
                        searchRecyclerView.isVisible = true
                        headerHistory.isVisible = true
                        setErrorVisibility(false)
                        searchRecyclerView.adapter = historySearchAdapter
                    }
                } else {
                    searchDebounce()
                    clearImageView.isVisible = true
                    searchRecyclerView.visibility = View.GONE
                    headerHistory.isVisible = false
                    clearHistoryButton.isVisible = false

                }
                searchQuery = searchEditText.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // nothing to do
            }

        }

        searchEditText.addTextChangedListener(textWatcher)


        // Get list of tracks


        // Repeat response if update button pressed
        updateButton.setOnClickListener() { _ ->
            makeResponse(lastSearch)
            searchEditText.setText(lastSearch)
            updateButton.visibility = View.GONE
            setErrorVisibility(false)
        }

    }

    private fun showAudioPlayerActivity(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(EXTRA_SELECTED_TRACK, track)
        startActivity(intent)
    }


}
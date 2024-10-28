package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView


import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create


class SearchActivity : AppCompatActivity() {

    private var searchQuery = ""
    private lateinit var searchEditText: EditText
    private val retrofit = Retrofit.Builder().baseUrl("https://itunes.apple.com").build()


    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

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

        //region initialize Views
        val backImageView = findViewById<MaterialToolbar>(R.id.back)
        searchEditText = findViewById(R.id.searchEditText)
        val clearImageView = findViewById<ImageView>(R.id.clear)
        val errorIcon = findViewById<ImageView>(R.id.errorIcon)
        val errorMessage = findViewById<TextView>(R.id.errorMessage)
        val updateButton = findViewById<MaterialButton>(R.id.updateButton)
        val iTunesApi = retrofit.create<ITunesApi>()
        val tracksList = mutableListOf<Track>()
        val clearHistoryButton = findViewById<MaterialButton>(R.id.clearHistoryButton)
        val headerHistory = findViewById<TextView>(R.id.headerHistory)
        //endregion
        val searchHistory = SearchHistory(getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE))
        var history = searchHistory.getTracksHistory()

        val searchRecyclerView = findViewById<RecyclerView>(R.id.searchRecyclerView)


        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && history.isNotEmpty()) {
                headerHistory.isVisible = true
                clearHistoryButton.isVisible = true
                searchRecyclerView.adapter = SearchAdapter(history.reversed(), {})
            }
        }

        //region History
        fun addTrackToHistory(track: Track) {
            history = searchHistory.getTracksHistory()
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

        // implement add to history in adapter
        val addTrackToHistory = SearchAdapter.AddToHistory { track ->
            addTrackToHistory(track)
            // add file to shared preferences
            searchHistory.saveTracksHistory(history)
            Log.d("HISTORY", "onCreate: ${searchHistory.getTracksHistory()}")
            Log.d("HISTORY", "Track added to history: ${track.trackName}")
            Toast.makeText(
                this,
                "${track.trackName} - ${track.artistName} добавлен",
                Toast.LENGTH_SHORT
            ).show()
        }
        //endregion

        // Add to recyclerview search results
        val searchAdapter = SearchAdapter(tracksList, addTrackToHistory)


        clearHistoryButton.setOnClickListener() { _ ->
            history.clear()
            searchHistory.clearHistory()
            searchRecyclerView.adapter?.notifyDataSetChanged()
            headerHistory.isVisible = false
            clearHistoryButton.isVisible = false
        }

        // set blank or restored text in search field
        searchEditText.setText(searchQuery)
        Log.d("Search", "onCreate searchQuery: $searchQuery")

        // clear search field
        clearImageView.setOnClickListener() { view ->
            searchEditText.setText("")
            headerHistory.isVisible = false
            tracksList.clear()
            searchAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // close SearchActivity
        backImageView.setNavigationOnClickListener {
            finish()
        }

        // Show or hide clear button for text search
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nothing to do
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // button is invisible if search text field empty
                if (!p0.isNullOrEmpty()) {
                    clearImageView.isVisible
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

        var lastSearch = ""

        // Get list of tracks
        fun makeResponse(text: String) {
            iTunesApi.search(text).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        searchRecyclerView.isVisible = true

                        val responseBody = response.body()?.string()
                        val gson = Gson()
                        val searchResult = gson.fromJson(responseBody, SearchResult::class.java)
                        Log.d("RESPONSE", responseBody.toString())
                        if (searchResult.resultCount == 0) {
                            extracted()
                        } else {
                            errorIcon.visibility = View.GONE
                            errorMessage.visibility = View.GONE
                        }
                        tracksList.clear()
                        tracksList.addAll(searchResult.tracks)
                        Log.d("TRACKS", searchResult.tracks.toString())
                        searchAdapter.notifyDataSetChanged()
                    }
                }

                private fun extracted() {
                    errorIcon.setImageResource(R.drawable.no_search_results)
                    errorMessage.text = getString(R.string.no_results)
                    errorIcon.isVisible = true
                    errorMessage.isVisible = true
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("RESPONSE", t.toString())
                    searchRecyclerView.visibility = View.GONE
                    errorIcon.setImageResource(R.drawable.no_internet)
                    errorIcon.isVisible = true
                    errorMessage.text = getString(R.string.no_internet_check_connection)
                    errorMessage.isVisible = true
                    updateButton.isVisible = true
                    lastSearch = searchEditText.text.toString()
                }
            }
            )
        }
        // Repeat response if update button pressed
        updateButton.setOnClickListener() { _ ->
            makeResponse(lastSearch)
            searchEditText.setText(lastSearch)
            updateButton.visibility = View.GONE
            errorIcon.visibility = View.GONE
            errorMessage.visibility = View.GONE
        }

        // Search if virtual keyboard Enter pressed
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchRecyclerView.adapter = searchAdapter
                makeResponse(searchEditText.text.toString())
                true
            }
            false
        }
    }


}
package com.practicum.playlistmaker.presentation.model.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding


import com.practicum.playlistmaker.domain.entity.Track


import com.practicum.playlistmaker.presentation.model.ui.audio_player.AudioPlayerActivity
import com.practicum.playlistmaker.presentation.state.AdapterState
import com.practicum.playlistmaker.presentation.state.HistoryState
import com.practicum.playlistmaker.presentation.state.SearchState

const val EXTRA_SELECTED_TRACK = "EXTRA_SELECTED_TRACK"

class SearchActivity : AppCompatActivity() {

    private var searchQuery = ""

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }

    private var tracks = emptyList<Track>()

    lateinit var binding: ActivitySearchBinding

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.factory(application)
    }

    lateinit var historySearchAdapter: SearchAdapter

    var historyIsEmpty: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchAdapter = SearchAdapter(tracks) { track ->
            showAudioPlayerActivity(track)
            viewModel.addTrackToHistory(track)

            viewModel.saveTracksHistory()

            Log.d("HISTORY", "Track added to history: ${track.trackName}")
            Toast.makeText(
                this,
                "${track.trackName} - ${track.artistName} добавлен",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.getTracks().observe(this) { tracksList ->
            tracks = tracksList
            searchAdapter.updateList(tracksList)
        }

        viewModel.getAdapterState().observe(this) { adapterState ->
            when (adapterState) {
                is AdapterState.Loading -> {
                    val list = adapterState.history
                    historySearchAdapter = SearchAdapter(list) { track ->
                        showAudioPlayerActivity(track)
                    }
                }

                is AdapterState.AdapterUpdated -> {
                    historySearchAdapter.updateList(adapterState.history)
                }

                AdapterState.AdapterSearch -> {
                    binding.searchRecyclerView.adapter = searchAdapter
                    viewModel.makeResponse(binding.searchEditText.text.toString())
                }
            }

        }

        viewModel.getHistoryIsEmpty().observe(this) { isEmpty ->
            historyIsEmpty = isEmpty
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && !historyIsEmpty) {
                binding.headerHistory.isVisible = true
                binding.clearHistoryButton.isVisible = true
                binding.searchRecyclerView.adapter = historySearchAdapter
            } else {
                binding.clearHistoryButton.isVisible = false
            }
        }


        viewModel.getHistoryState().observe(this) { historyState ->
            when (historyState) {
                HistoryState.HistoryCleared -> TODO()
                is HistoryState.HistoryTrackAdded -> {
                    historySearchAdapter.updateList(historyState.list)
                    historySearchAdapter.notifyDataSetChanged()
                }
            }

        }

        viewModel.getSearchState().observe(this) { searchState ->
            when (searchState) {
                is SearchState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.searchRecyclerView.visibility = View.GONE
                    binding.errorIcon.setImageResource(R.drawable.no_internet)
                    binding.errorMessage.text =
                        getString(R.string.no_internet_check_connection)
                    setErrorVisibility(true)
                    binding.updateButton.isVisible = true
                    viewModel.setLastSearch(binding.searchEditText.text.toString())
                }



                SearchState.Responsing -> {
                    binding.progressBar.isVisible = true
                }

                SearchState.Result -> {
                    binding.progressBar.visibility = View.GONE
                    setErrorVisibility(false)
                    binding.searchRecyclerView.adapter = searchAdapter
                    searchAdapter.notifyDataSetChanged()
                    binding.searchRecyclerView.isVisible = true

                }

                SearchState.ResultEmpty -> {
                    binding.progressBar.visibility = View.GONE
                    binding.errorIcon.setImageResource(R.drawable.no_search_results)
                    binding.errorMessage.text = getString(R.string.no_results)
                    setErrorVisibility(true)
                    searchAdapter.notifyDataSetChanged()
                    binding.searchRecyclerView.isVisible = true
                }

            }

        }

        binding.clearHistoryButton.setOnClickListener() { _ ->
            viewModel.clear()
            viewModel.clearHistory()
            viewModel.onClickHistoryButton()

            viewModel.onClickHistoryButton()
            historySearchAdapter.notifyDataSetChanged()
            binding.headerHistory.isVisible = false
            binding.clearHistoryButton.isVisible = false

        }

        // set blank or restored text in search field
        binding.searchEditText.setText(searchQuery)
        Log.d("Search", "onCreate searchQuery: $searchQuery")

        // clear search field
        binding.clearImageView.setOnClickListener() { view ->
            binding.searchEditText.setText("")
            //viewModel.clear()
            setErrorVisibility(false)
            if (!historyIsEmpty) {
                binding.searchRecyclerView.isVisible = true
            }
            searchAdapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        // close SearchActivity
        binding.backImageView.setNavigationOnClickListener {
            finish()
        }

        // Show or hide clear button for text search
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nothing to do
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // button is invisible if search text field empty

                if (p0.isNullOrEmpty()) {

                    binding.clearImageView.isVisible = false
                    binding.updateButton.isVisible = false
                    if (!historyIsEmpty) {
                        binding.clearHistoryButton.isVisible = true
                        binding.searchRecyclerView.isVisible = true
                        binding.headerHistory.isVisible = true
                        setErrorVisibility(false)
                        binding.searchRecyclerView.adapter = historySearchAdapter
                    }
                } else {

                    viewModel.searchDebounce()
                    binding.clearImageView.isVisible = true
                    binding.searchRecyclerView.visibility = View.GONE
                    binding.headerHistory.isVisible = false
                    binding.clearHistoryButton.isVisible = false

                }
                searchQuery = binding.searchEditText.text.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // nothing to do
            }

        }

        binding.searchEditText.addTextChangedListener(textWatcher)


        // Repeat response if update button pressed
        binding.updateButton.setOnClickListener() { _ ->
            viewModel.updateButtonClicked()

            //  binding.searchEditText.setText(lastSearch)
            binding.updateButton.visibility = View.GONE
            setErrorVisibility(false)
        }

    }

    fun setErrorVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.errorIcon.isVisible = true
            binding.errorMessage.isVisible = true
        } else {
            binding.errorIcon.visibility = View.GONE
            binding.errorMessage.visibility = View.GONE
        }
    }


    private fun showAudioPlayerActivity(track: Track) {
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(EXTRA_SELECTED_TRACK, track)
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
        Log.d("Search", "onSaveInstanceState searchQuery: $searchQuery")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH_QUERY).toString()
        binding.searchEditText.setText(searchQuery)
        Log.d("Search", "onRestoreInstanceState searchQuery: $searchQuery")

    }

}
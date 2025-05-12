package com.practicum.playlistmaker.search.ui


import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audio_player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


const val EXTRA_SELECTED_TRACK = "EXTRA_SELECTED_TRACK"

class SearchFragment : Fragment() {

    val TAG = "DEBUG"

    private var searchQuery = ""

    companion object {
        fun newInstance() = SearchFragment()
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModel<SearchViewModel>()

    fun showHistory(show: Boolean) {
        if (show) {
            binding.headerHistory.isVisible = true
            binding.clearHistoryButton.isVisible = true
            binding.searchRecyclerView.isVisible = true
        } else {
            binding.headerHistory.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
            binding.searchRecyclerView.visibility = View.GONE
        }
    }

    fun setAdapter(list: List<Track>) {
        binding.searchRecyclerView.adapter = SearchAdapter(list) { track ->
            showAudioPlayerActivity(track)
            viewModel.addTrackToHistory(track)
            viewModel.saveTracksHistory()
            Log.d("DEBUG", "Track added to history: ${track.trackName}")
            Toast.makeText(
                requireContext(),
                "${track.trackName} - ${track.artistName} добавлен",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                Log.d(TAG, "onCreate: hasFocus")
                viewModel.searchEditTextClicked((view as EditText).text)
            }
        }


        viewModel.getSearchState().observe(viewLifecycleOwner) { searchState ->
            when (searchState) {
                is SearchState.TextFieldClicked -> {
                    if (searchState.fieldIsEmpty) {
                        Log.d(TAG, "fieldIsEmpty = true")
                        if (searchState.historyIsEmpty) {
                            Log.d(TAG, "historyIsEmpty = true")
                            showHistory(false)
                        } else {
                            Log.d(TAG, "historyIsEmpty = false")
                            setAdapter(searchState.history)
                            binding.searchRecyclerView.isVisible = true
                            showHistory(true)
                        }
                    } else {
                        Log.d(TAG, "fieldIsEmpty = false")
                        binding.clearImageView.isVisible = true

                    }
                }

                SearchState.Searching -> search()
                is SearchState.Error -> showError(true, searchState.message)
                is SearchState.Result -> showResult(searchState.list)
                is SearchState.Loading -> loading(searchState.history)
                is SearchState.HistoryCleared -> clearHistory()
                is SearchState.FieldCleared -> fieldCleared(searchState.history)
            }

        }


        binding.clearHistoryButton.setOnClickListener() { _ ->
            viewModel.clearHistory()

        }

        Log.d("Search", "onCreate searchQuery: $searchQuery")

        // clear search field
        binding.clearImageView.setOnClickListener() { view ->
            binding.searchEditText.setText("")
            viewModel.clearField()
            val inputMethodManager =
                requireActivity().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
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
                    viewModel.deleteResponse()

                } else {
                    binding.clearImageView.isVisible = true

                    viewModel.searchDebounce(p0.toString())

                }

            }

            override fun afterTextChanged(p0: Editable?) {
                // nothing to do
            }

        }

        binding.searchEditText.addTextChangedListener(textWatcher)


        // Repeat response if update button pressed
        binding.updateButton.setOnClickListener() { _ ->
            viewModel.makeResponse(binding.searchEditText.text.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    private fun fieldCleared(history: List<Track>) {
        showError(false, null)
        setAdapter(history)
        binding.searchRecyclerView.isVisible = true

    }

    private fun clearHistory() {
        Log.d(TAG, "clearHistory: ")
        showHistory(false)
    }

    private fun showResult(list: List<Track>) {
        Log.d(TAG, "showResult: $list")
        binding.progressBar.visibility = View.GONE
        showError(false, null)
        binding.searchRecyclerView.isVisible = true
        if (list.isEmpty()) {
            binding.errorIcon.setImageResource(R.drawable.no_search_results)
            showError(true, getString(R.string.no_results))
        } else {
            setAdapter(list)
        }

    }

    private fun showError(show: Boolean, message: String?) {
        Log.d(TAG, "showError: $message")
        binding.progressBar.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE
        if (show) {
            binding.errorMessage.text = message
            binding.errorIcon.isVisible = true
            binding.errorMessage.isVisible = true
            binding.updateButton.isVisible = true
        } else {
            binding.errorIcon.visibility = View.GONE
            binding.errorMessage.visibility = View.GONE
            binding.updateButton.visibility = View.GONE
        }

    }

    private fun search() {
        showError(false, null)
        Log.d(TAG, "search: ")
        binding.progressBar.isVisible = true
        showHistory(false)
    }

    private fun loading(history: List<Track>) {
        Log.d(TAG, "loading: $history")
        binding.searchRecyclerView.adapter = SearchAdapter(history) { track ->
            showAudioPlayerActivity(track)
        }
        binding.searchRecyclerView.isVisible = false

    }

    private fun showAudioPlayerActivity(track: Track) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra(EXTRA_SELECTED_TRACK, track)
        startActivity(intent)
    }

}
package com.practicum.playlistmaker.medialibrary.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.audio_player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.FragmentFavoritesBinding
import com.practicum.playlistmaker.search.ui.EXTRA_SELECTED_TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel by viewModel<FavoritesViewModel>()

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner) {
            state ->
            when(state){
                is FavoritesListState.Content -> showContent(state.favoritesTrackList)
                FavoritesListState.Empty -> showEmpty()
                is FavoritesListState.Error -> showError()
                FavoritesListState.Loading -> showLoading()
            }
        }


    }

    private fun showLoading() {

    }

    private fun showError() {

    }

    private fun showEmpty() {
        binding.favoritesRecyclerView.visibility = View.GONE
        binding.errorIcon.isVisible = true
        binding.errorMessage.isVisible = true
    }

    private fun showContent(favoritesTrackList: List<Track>) {
        binding.favoritesRecyclerView.isVisible = true
        binding.favoritesRecyclerView.adapter = FavoritesAdapter(favoritesTrackList){ track ->
            showAudioPlayerActivity(track)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadFavoriteTracks()
    }

    private fun showAudioPlayerActivity(track: Track) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra(EXTRA_SELECTED_TRACK, track)
        startActivity(intent)
    }

}
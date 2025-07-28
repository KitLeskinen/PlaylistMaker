package com.practicum.playlistmaker.audio_player.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.AudioplayerFragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetPlaylistsFragment() : Fragment() {

    companion object {


        const val SELECTED_TRACK_ID_KEY = "SELECTED_TRACK_ID_KEY"

        fun newInstance(selectedTrackID: Track) = BottomSheetPlaylistsFragment().apply {
            arguments = bundleOf(SELECTED_TRACK_ID_KEY to selectedTrackID)
        }
    }

    private var _binding: AudioplayerFragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<BottomSheetPlaylistViewModel>()

    private var selectedTrack: Track? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AudioplayerFragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BottomSheetPlaylistState.Loading -> load(state.playlists)
                is BottomSheetPlaylistState.TrackAdded -> trackAdded(state.result, state.name, state.playlists)
            }
        }

    }

    private fun trackAdded(result: Long, playlistName: String, playlists: List<Playlist>?) {
        Log.d("TAG", "result: $result")
        if (result == -1L) {
            Toast.makeText(
                requireContext(),
                "${resources.getString(R.string.already_added_to_playlist)} ${playlistName}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (playlists != null) {
                load(playlists)
            }
            Toast.makeText(
                requireContext(),
                "${resources.getString(R.string.added_to_playlist)} ${playlistName}",
                Toast.LENGTH_SHORT
            ).show()
            (activity as? AudioPlayerActivity)?.collapseBottomSheet()
        }
    }

    private fun load(playlists: List<Playlist>) {
        binding.playlistRecyclerView.adapter = BottomSheetPlaylistAdapter(playlists, { playlist ->
            selectedTrack?.let { viewModel.addTrackToPlayList(it, playlist) }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylistItems()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedTrack = arguments?.getSerializable(SELECTED_TRACK_ID_KEY) as? Track
    }

}
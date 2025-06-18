package com.practicum.playlistmaker.audio_player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.AudioplayerFragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetPlaylistsFragment() : Fragment() {

    companion object {


        const val SELCTED_TRACK_ID_KEY = "SELCTED_TRACK_ID_KEY"

        fun newInstance(selectedTrackID: Track) = BottomSheetPlaylistsFragment().apply {
            arguments = bundleOf(SELCTED_TRACK_ID_KEY to selectedTrackID)
        }
    }

    private var _binding: AudioplayerFragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<BottomSheetPlaylistViewModel>()

    var selectedTrack: Track? = null

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

//        binding.playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        selectedTrack = arguments?.getSerializable(SELCTED_TRACK_ID_KEY) as? Track

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is BottomSheetPlaylistState.Loading -> load(state.playlists)
            }
        }

    }

    private fun load(playlists: List<Playlist>) {
        binding.playlistRecyclerView.adapter = BottomSheetPlaylistAdapter(playlists, { playlist ->
            Toast.makeText(requireContext(), "${playlist.name} ${selectedTrack?.trackName}", Toast.LENGTH_SHORT).show()
            selectedTrack?.let { viewModel.addTrackToPlayList(it, playlist) }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylistItems()


    }
}
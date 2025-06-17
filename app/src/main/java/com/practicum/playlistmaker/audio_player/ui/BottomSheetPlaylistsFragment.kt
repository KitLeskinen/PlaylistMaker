package com.practicum.playlistmaker.audio_player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.databinding.AudioplayerFragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetPlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = BottomSheetPlaylistsFragment()
    }

    private var _binding: AudioplayerFragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<BottomSheetPlaylistViewModel>()


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


        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is BottomSheetPlaylistState.Loading -> load(state.playlists)
            }
        }

    }

    private fun load(playlists: List<Playlist>) {
        binding.playlistRecyclerView.adapter = BottomSheetPlaylistAdapter(playlists)
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylistItems()


    }
}
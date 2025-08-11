package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistButton.setOnClickListener() {
            findNavController().navigate(R.id.fragmentNewPlaylist)
        }
        binding.playlistRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)


        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.Loading -> load(state.playlists)
            }
        }
    }

    private fun load(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            binding.playlistRecyclerView.visibility = View.GONE
            binding.errorIcon.visibility = View.VISIBLE
            binding.errorMessage.visibility = View.VISIBLE
        } else {
            binding.playlistRecyclerView.visibility = View.VISIBLE
            binding.playlistRecyclerView.adapter = PlaylistAdapter(playlists) { playlist ->
                val directions: NavDirections =
                    MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistScreenFragment(
                        playlist
                    )
                findNavController().navigate(directions)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updatePlaylistItems()
    }
}
package com.practicum.playlistmaker.medialibrary.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.practicum.playlistmaker.audio_player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.practicum.playlistmaker.search.ui.EXTRA_SELECTED_TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlaylistScreenFragment : Fragment() {
    private val args: PlaylistScreenFragmentArgs by navArgs()

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<PlaylistScreenViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnClickListener() {
            findNavController().popBackStack()
        }
        Toast.makeText(requireContext(), "${args.playlist.trackList?.size}", Toast.LENGTH_SHORT)
            .show()
        viewModel.loadPlaylist(args.playlist)

        args.playlist.trackList?.forEach { track: Track ->
            Log.d("TAG", "onViewCreated: $track")
        }

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistScreenState.Loading -> fillPlaylistViews(state.playlist)
            }
        }

    }

    private fun fillPlaylistViews(playlist: Playlist) {
        binding.playlistNameTextView.text = playlist.name
        binding.playlistDescriptionTextView.text = playlist.description
        binding.coverImage.setImageURI(Uri.parse(playlist.coverUri))
        binding.playlistTracksRecyclerView.adapter =
            playlist.trackList?.let { FavoritesAdapter(it) { track -> showAudioPlayerActivity(track) } }
    }

    private fun showAudioPlayerActivity(track: Track) {
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java)
        intent.putExtra(EXTRA_SELECTED_TRACK, track)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(
            inflater, container, false
        )
        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
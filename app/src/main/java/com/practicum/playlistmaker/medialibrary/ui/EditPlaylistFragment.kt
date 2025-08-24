package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : NewPlaylistFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override val viewModel: EditPlaylistViewModel by viewModel()

    private val args: EditPlaylistFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "${args.playlistId}", Toast.LENGTH_SHORT).show()

    }

    override fun load() {
        super.load()
        viewModel.setPlaylistId(args.playlistId)

    }

    override fun fillViews(coverUri: String?, name: String, description: String) {
        super.fillViews(coverUri, name, description)
        Glide.with(binding.addPlayListCoverButton).load(coverUri).centerCrop().placeholder(R.drawable.placeholder).into(binding.addPlayListCoverButton)


        binding.playlistNameEditText.setText(name)
        binding.playlistDescriptionEditText.setText(description)

    }

    override fun createPlaylist(): Playlist {
        val playlist = super.createPlaylist()
        return Playlist(args.playlistId, playlist.name, playlist.description, playlist.coverUri, playlist.trackList)
    }

    override fun backAction() {
        findNavController().popBackStack()
    }
}
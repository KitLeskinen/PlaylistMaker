package com.practicum.playlistmaker.medialibrary.ui

import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Tools
import com.practicum.playlistmaker.audio_player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.common.data.domain.entity.Playlist
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.practicum.playlistmaker.search.ui.EXTRA_SELECTED_TRACK
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class PlaylistScreenFragment : Fragment() {
    private val args: PlaylistScreenFragmentArgs by navArgs()

    lateinit var playlist: Playlist


    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel by viewModel<PlaylistScreenViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backImageView.setOnClickListener() {
            findNavController().popBackStack()
        }

        viewModel.loadPlaylist(args.playlistId)

        BottomSheetBehavior.from(binding.bottomSheetMeatballs).state = BottomSheetBehavior.STATE_HIDDEN

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistScreenState.Loading -> {
                    fillPlaylistViews(state.playlist, state.minutes)
                    playlist = state.playlist

                }
            }
        }



        binding.meatballsMenu.setOnClickListener {
            BottomSheetBehavior.from(binding.bottomSheetMeatballs).state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.shareTextView

        fun share() {
            if (playlist.trackList?.size ?: 0 == 0) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.in_this_playlist_no_tracks_to_share), Toast.LENGTH_SHORT
                ).show()
            } else {

                var message: String =
                    "${playlist.name}\n${playlist.description}\n${playlist.trackList?.size} ${
                        Tools.declensions(
                            requireContext(),
                            playlist.trackList?.size ?: 0
                        )
                    }"
                playlist.trackList?.forEachIndexed { index, track ->
                    message += "\n${index + 1}. ${track.trackName} - ${track.artistName} (${
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(track.trackTime)
                    })"
                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, message)
                intent.setType("text/plain")
                startActivity(intent)

            }
        }

        binding.shareImageButton.setOnClickListener {
            share()
        }
        binding.shareTextView.setOnClickListener {
            share()
        }

        binding.editInfoTextView.setOnClickListener{

            val directions: NavDirections =
                MediaLibraryFragmentDirections.actionMediaLibraryFragmentToPlaylistScreenFragment(
                    playlist.id
                )
            findNavController().navigate(directions)



        }

        binding.deletePlaylistTextView.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext()).setMessage(getString(R.string.do_you_want_to_delete_playlist))
                .setPositiveButton("Да", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        viewModel.deletePlaylist(playlist.id)
                        findNavController().popBackStack()

                    }
                }).setNeutralButton("Нет", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {

                    }
                }).show()


        }


    }

    private fun dialogToDeleteTrack(track: Track) {
        MaterialAlertDialogBuilder(requireContext()).setMessage(getString(R.string.do_you_want_to_delete_track))
            .setPositiveButton("Да", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    viewModel.deleteTrack(track)

                }
            }).setNeutralButton("Нет", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            }).show()
    }

    private fun fillPlaylistViews(playlist: Playlist, minutes: Long) {
        binding.playlistNameTextView.text = playlist.name
        binding.playlistDescriptionTextView.text = playlist.description
        binding.coverImage.setImageURI(Uri.parse(playlist.coverUri))
        binding.bottomSheetPlaylistName.text = playlist.name
        binding.bottomSheetTrackCover.setImageURI(Uri.parse(playlist.coverUri))
        binding.bottomSheetTracksCount.text = "${playlist.trackList?.size} ${
            playlist.trackList?.let {
                Tools.declensions(
                    requireContext(),
                    it.size
                )
            }
        }"
        binding.playlistTracksRecyclerView.adapter =
            playlist.trackList?.let {
                PlaylistScreenAdapter(
                    it,
                    onLongClick = { track ->
                        dialogToDeleteTrack(track)
                    },
                    onClick = { track -> showAudioPlayerActivity(track) }

                )
            }
        val tracksCount: Int = playlist.trackList?.size ?: 0
        binding.playlistDurationAndCountTextView.text = "$tracksCount ${
            Tools.declensions(
                requireContext(),
                tracksCount
            )
        } • $minutes ${Tools.declensionsMinutes(requireContext(), minutes.toInt())}"
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
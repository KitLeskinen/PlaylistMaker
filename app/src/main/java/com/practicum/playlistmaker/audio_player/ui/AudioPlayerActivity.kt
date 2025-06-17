package com.practicum.playlistmaker.audio_player.ui


import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Tools
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

const val EXTRA_SELECTED_TRACK = "EXTRA_SELECTED_TRACK"

class AudioPlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var selectedTrack: Track

    private val viewModel: AudioPlayerViewModel by viewModel {
        parametersOf(selectedTrack)
    }

    private var previewUrl: String? = null

    private fun preparePlayer() {
        binding.mediaButton.isEnabled = true
    }

    private fun startPlayer() {
        binding.mediaButton.setImageResource(R.drawable.pause_button)
    }

    private fun pausePlayer() {
        binding.mediaButton.setImageResource(R.drawable.play_button)

    }

    private fun stoppedPlayer() {
        binding.currentPlayTimeTextView.text = "00:00"
        binding.mediaButton.setImageResource(R.drawable.play_button)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    private fun fillInPlayerFields(track: Track) {
        val formatFateFromJSON = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val yearFormat = SimpleDateFormat("yyyy")
        previewUrl = track.previewUrl
        binding.trackNameTextView.text = track.trackName
        binding.artistNameTextView.text = track.artistName
        Glide.with(binding.coverImage).load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder).transform(
                CenterCrop(),
                RoundedCorners(Tools.dpToPx(8f, binding.coverImage.context))
            ).into(binding.coverImage)
        binding.durationTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
            track.trackTime
        )
        binding.albumNameTextView.setText(track.collectionName)
        val date = formatFateFromJSON.parse(track.releaseDate)
        binding.songGenreTextView.text = track.primaryGenreName
        binding.countryTextView.text = track.country
        binding.albumYearTextView.text = yearFormat.format(date)
    }

    fun updateTimePosition(position: Int) {
        binding.currentPlayTimeTextView.text = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(position)
        Log.d("POSITION", "onCreate: $position")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentBottomSheet) as NavHostFragment
        val navController = navHostFragment.navController

        selectedTrack = intent.getSerializableExtra(EXTRA_SELECTED_TRACK) as Track
        binding.backImageView.setNavigationOnClickListener {
            finish()
        }

      //  navController.navigate(R.id.fragmentBottomSheetNewPlaylist)

        onBackPressedDispatcher.addCallback(this) {

            val currentDestinationId = navController.currentDestination?.id
            val dest = navController.currentDestination
            Log.d(
                "NAV",
                "Current dest: ${dest?.id}, name: ${dest?.navigatorName}, label: ${dest?.label}"
            )

            if (currentDestinationId == R.id.audioplayerBottomSheetFragmentPlaylists) {
                Log.d("BACK", "Находимся в NewPlaylistFragment")
                navController.popBackStack()
                binding.fragmentBottomSheet.visibility = View.GONE
                binding.newPlaylistButton.visibility = View.VISIBLE

                BottomSheetBehavior.from(binding.bottomSheet).state =
                    BottomSheetBehavior.STATE_COLLAPSED
            } else {
                finish()
            }
        }

        binding.mediaButton.setOnClickListener {
            viewModel.togglePlayer()
        }

        viewModel.getState().observe(this) { state ->
            when (state) {
                is AudioPlayerState.Content -> TODO()
                is AudioPlayerState.Loading -> {
                    fillInPlayerFields(state.track)
                }

                AudioPlayerState.Paused -> pausePlayer()

                AudioPlayerState.StartPlaying -> startPlayer()

                AudioPlayerState.Preparing -> preparePlayer()

                AudioPlayerState.Stopped -> stoppedPlayer()

                is AudioPlayerState.Playback -> updateTimePosition(state.timePositionState)


            }
        }
        viewModel.getFavoritesState().observe(this) { state ->
            when (state) {
                is FavoritesState.FavoritesChanged -> {
                    if (state.isFavorite) {
                        binding.favoritesButton.setImageResource(R.drawable.favorites_button_active)
                    } else {
                        binding.favoritesButton.setImageResource(R.drawable.favorites_button_not_active)
                    }
                }
            }
        }

        binding.favoritesButton.setOnClickListener {
            viewModel.switchFavorites()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)

        binding.newPlaylistButton.setOnClickListener() {
            Log.d("TAG", "onCreate:   binding.addToPlaylistButton.setOnClickListener")

//            navController.navigate(R.id.fragmentBottomSheetNewPlaylist)
            navController.navigate(R.id.audioplayerBottomSheetFragmentPlaylists)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.newPlaylistButton.visibility = View.GONE
            binding.fragmentBottomSheet.visibility = View.VISIBLE

        }
    }


}
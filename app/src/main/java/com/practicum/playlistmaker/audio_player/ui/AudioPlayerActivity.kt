package com.practicum.playlistmaker.audio_player.ui

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Tools
import com.practicum.playlistmaker.common.data.domain.entity.Track
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding


import java.util.Locale

const val EXTRA_SELECTED_TRACK = "EXTRA_SELECTED_TRACK"

class AudioPlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAudioplayerBinding

    private val viewModel: AudioPlayerViewModel by viewModels {
        val selectedTrack = intent.getSerializableExtra(EXTRA_SELECTED_TRACK) as Track
        AudioPlayerViewModel.factory(
            selectedTrack,
            Creator.provideAudioPlayerInteractor(selectedTrack)
        )
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

    fun updateTimePosition(position: Int){
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
        binding.backImageView.setNavigationOnClickListener {
            finish()
        }

        binding.mediaButton.setOnClickListener {
            viewModel.togglePlayer()
        }

        viewModel.getState().observe(this) { state ->
            when (state) {
                is AudioPlayerState.Content -> TODO()
                AudioPlayerState.Loading -> {
                    fillInPlayerFields(viewModel.getTrack())
                }

                AudioPlayerState.Paused -> pausePlayer()

                AudioPlayerState.StartPlaying -> startPlayer()

                AudioPlayerState.Preparing -> preparePlayer()

                AudioPlayerState.Stopped -> stoppedPlayer()

                is AudioPlayerState.Playback -> updateTimePosition(state.timePositionState)
            }
        }
//        viewModel.getTimePositionState().observe(this) { position ->
//
//
//        }

    }


}
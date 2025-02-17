package com.practicum.playlistmaker.presentation.model.ui.audio_player

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Tools
import com.practicum.playlistmaker.domain.api.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.OnCompletionListener
import com.practicum.playlistmaker.domain.api.OnPreparedAudioPlayerListener
import com.practicum.playlistmaker.domain.entity.Track

import java.util.Locale

const val EXTRA_SELECTED_TRACK = "EXTRA_SELECTED_TRACK"

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var audioPlayerInteractor: AudioPlayerInteractor

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }

    private var handler: Handler? = null
    private lateinit var currentPlayTimeTextView: TextView

    private var previewUrl: String? = null

    private var playerState = STATE_DEFAULT

    private lateinit var mediaButton: ImageButton


    private fun preparePlayer() {

        audioPlayerInteractor.prepare(
            onPreparedAudioPlayerListener = object : OnPreparedAudioPlayerListener {
                override fun invoke() {
                    mediaButton.isEnabled = true
                    playerState = STATE_PREPARED
                }

            },
            onCompletionListener = object : OnCompletionListener {
                override fun invoke() {
                    currentPlayTimeTextView.text = "00:00"
                    playerState = STATE_PREPARED
                    mediaButton.setImageResource(R.drawable.play_button)
                }

            }
        )


    }

    private fun startPlayer() {

        audioPlayerInteractor.play()
        mediaButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {

        audioPlayerInteractor.pause()
        mediaButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)


        val backImageView = findViewById<MaterialToolbar>(R.id.back)

        val intent = getIntent()

        val selectedTrack = intent.getSerializableExtra(EXTRA_SELECTED_TRACK) as Track

        audioPlayerInteractor = Creator.provideAudioPlayerInteractor(selectedTrack)



        previewUrl = selectedTrack.previewUrl
        val coverImage = findViewById<ImageView>(R.id.coverImage)
        val trackNameTextView = findViewById<TextView>(R.id.trackNameTextView)
        val artistNameTextView = findViewById<TextView>(R.id.artistNameTextView)
        val durationTextView = findViewById<TextView>(R.id.durationTextView)
        val albumNameTextView = findViewById<TextView>(R.id.albumNameTextView)
        val albumYearTextView = findViewById<TextView>(R.id.albumYearTextView)
        val songGenreTextView = findViewById<TextView>(R.id.songGenreTextView)
        val countryTextView = findViewById<TextView>(R.id.countryTextView)
        currentPlayTimeTextView = findViewById(R.id.currentPlayTimeTextView)

        handler = Handler(Looper.getMainLooper())

        mediaButton = findViewById(R.id.mediaButton)


        preparePlayer()

        Glide.with(coverImage).load(selectedTrack.getCoverArtwork())
            .placeholder(R.drawable.placeholder).transform(
                RoundedCorners(Tools.dpToPx(8f, coverImage.context))
            ).into(coverImage)


        trackNameTextView.text = selectedTrack.trackName

        artistNameTextView.text = selectedTrack.artistName

        durationTextView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
            selectedTrack.trackTime
        )

        albumNameTextView.setText(selectedTrack.collectionName)

        val formatFateFromJSON = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val yearFormat = SimpleDateFormat("yyyy")
        val date = formatFateFromJSON.parse(selectedTrack.releaseDate)

        albumYearTextView.text = yearFormat.format(date)
        songGenreTextView.text = selectedTrack.primaryGenreName
        countryTextView.text = selectedTrack.country

        backImageView.setNavigationOnClickListener {
            finish()
        }

        mediaButton.setOnClickListener {
            playbackControl()
            handler?.post(createCurrentPlayTimer())

        }

    }


    private fun createCurrentPlayTimer(): Runnable {
        return object : Runnable {
            override fun run() {

                if (playerState == STATE_PLAYING) {
                    currentPlayTimeTextView.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(audioPlayerInteractor.getCurrentPosition())

                    handler?.postDelayed(this, DELAY)
                }

            }

        }
    }
}
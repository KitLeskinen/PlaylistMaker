package com.practicum.playlistmaker

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        val backImageView = findViewById<MaterialToolbar>(R.id.back)

        val intent = getIntent()

        val selectedTrack = intent.getSerializableExtra(EXTRA_SELECTED_TRACK) as Track


        val coverImage = findViewById<ImageView>(R.id.coverImage)
        val trackNameTextView = findViewById<TextView>(R.id.trackNameTextView)
        val artistNameTextView = findViewById<TextView>(R.id.artistNameTextView)
        val durationTextView = findViewById<TextView>(R.id.durationTextView)
        val albumNameTextView = findViewById<TextView>(R.id.albumNameTextView)
        val albumYearTextView = findViewById<TextView>(R.id.albumYearTextView)
        val songGenreTextView = findViewById<TextView>(R.id.songGenreTextView)
        val countryTextView = findViewById<TextView>(R.id.countryTextView)


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

    }
}
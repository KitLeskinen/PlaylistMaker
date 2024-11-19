package com.practicum.playlistmaker

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val trackId = intent.getLongExtra("trackId", 0)
        val trackName = intent.getStringExtra("trackName")
        val artistName = intent.getStringExtra("artistName")
        val collectionName = intent.getStringExtra("collectionName")
        val releaseDate = intent.getStringExtra("releaseDate")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val trackTimeMills = intent.getLongExtra("trackTimeMills", 0)
        val albumCover = intent.getStringExtra("albumCover")


        val coverImage = findViewById<ImageView>(R.id.coverImage)
        val trackNameTextView = findViewById<TextView>(R.id.trackNameTextView)
        val artistNameTextView = findViewById<TextView>(R.id.artistNameTextView)
        val durationTextView = findViewById<TextView>(R.id.durationTextView)
        val albumNameTextView = findViewById<TextView>(R.id.albumNameTextView)
        val albumYearTextView = findViewById<TextView>(R.id.albumYearTextView)
        val songGenreTextView = findViewById<TextView>(R.id.songGenreTextView)
        val countryTextView = findViewById<TextView>(R.id.countryTextView)


        Glide.with(coverImage).load(albumCover?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder).transform(
                RoundedCorners(Tools.dpToPx(8f, coverImage.context))
            ).into(coverImage)


        trackNameTextView.setText(trackName)

        artistNameTextView.setText(artistName)

        durationTextView.setText(
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                trackTimeMills
            )
        )

        albumNameTextView.setText(collectionName)


        val formatFateFromJSON = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val yearFormat = SimpleDateFormat("yyyy")
        val date = formatFateFromJSON.parse(releaseDate)

        albumYearTextView.setText(yearFormat.format(date))

        songGenreTextView.setText(primaryGenreName)

        countryTextView.setText(country)



        Log.d(
            "INTENT", """onCreate: 
            | trackId $trackId
            | trackName: $trackName 
            | artistName: $artistName
            | collectionName: $collectionName
            | releaseDate: $releaseDate 
            | primaryGenreName: $primaryGenreName
            | country: $country
            | trackTimeMills:$trackTimeMills""".trimMargin()
        )

        backImageView.setNavigationOnClickListener {
            finish()
        }

    }
}
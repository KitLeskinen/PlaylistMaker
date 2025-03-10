package com.practicum.playlistmaker.presentation.model.ui.main_menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.practicum.playlistmaker.presentation.model.ui.media_library.MediaLibrary
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.model.ui.settings.SettingsActivity
import com.practicum.playlistmaker.presentation.model.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)
        val mediaLibraryButton = findViewById<Button>(R.id.medialibrary)
        val settingsButton = findViewById<Button>(R.id.settings)

        val buttonOnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }

        }
        searchButton.setOnClickListener(buttonOnClickListener)


        mediaLibraryButton.setOnClickListener {
            val intent = Intent(this, MediaLibrary::class.java)
            startActivity(intent)
        }
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }


}
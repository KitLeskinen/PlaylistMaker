package com.practicum.playlistmaker.presentation.model.ui.main_menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.practicum.playlistmaker.presentation.model.ui.media_library.MediaLibrary
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.presentation.model.ui.settings.SettingsActivity
import com.practicum.playlistmaker.presentation.model.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val buttonOnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }

        }
        binding.searchButton.setOnClickListener(buttonOnClickListener)

        binding.mediaLibraryButton.setOnClickListener {
            val intent = Intent(this, MediaLibrary::class.java)
            startActivity(intent)
        }
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }


}
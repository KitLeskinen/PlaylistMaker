package com.practicum.playlistmaker.main_menu.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.practicum.playlistmaker.medialibrary.ui.MediaLibraryFragment
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.settings.ui.SettingsFragment
import com.practicum.playlistmaker.search.ui.SearchFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val buttonOnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@MainActivity, SearchFragment::class.java)
                startActivity(intent)
            }

        }
        binding.searchButton.setOnClickListener(buttonOnClickListener)

        binding.mediaLibraryButton.setOnClickListener {
            val intent = Intent(this, MediaLibraryFragment::class.java)
            startActivity(intent)
        }
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsFragment::class.java)
            startActivity(intent)
        }

    }


}
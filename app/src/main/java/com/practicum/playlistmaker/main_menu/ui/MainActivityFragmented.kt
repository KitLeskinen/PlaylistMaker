package com.practicum.playlistmaker.main_menu.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.medialibrary.ui.MediaLibraryFragment
import com.practicum.playlistmaker.search.ui.SearchFragment
import com.practicum.playlistmaker.settings.ui.SettingsFragment

class MainActivityFragmented : AppCompatActivity(R.layout.activity_main_fragmented) {

//    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
            supportFragmentManager.commit {
                add(R.id.fragment_container_view, MediaLibraryFragment())
            }

        }
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//
//        setContentView(binding.root)
//
//        val buttonOnClickListener: View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(p0: View?) {
//                val intent = Intent(this@MainActivityFragmented, SearchActivity::class.java)
//                startActivity(intent)
//            }
//
//        }
//        binding.searchButton.setOnClickListener(buttonOnClickListener)
//
//        binding.mediaLibraryButton.setOnClickListener {
//            val intent = Intent(this, MediaLibraryActivity::class.java)
//            startActivity(intent)
//        }
//        binding.settingsButton.setOnClickListener {
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//        }
//
//    }


}
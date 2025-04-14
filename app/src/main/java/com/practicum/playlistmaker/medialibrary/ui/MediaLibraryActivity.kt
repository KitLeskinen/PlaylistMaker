package com.practicum.playlistmaker.medialibrary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMedialibraryBinding


class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedialibraryBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedialibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
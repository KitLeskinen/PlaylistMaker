package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.material.appbar.MaterialToolbar


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backImageView = findViewById<MaterialToolbar>(R.id.back)

        backImageView.setNavigationOnClickListener{
            finish()
        }


    }
}
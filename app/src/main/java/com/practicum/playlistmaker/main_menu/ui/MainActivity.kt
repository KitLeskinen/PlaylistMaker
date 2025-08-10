package com.practicum.playlistmaker.main_menu.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R


class MainActivity : AppCompatActivity(R.layout.activity_main) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id == R.id.fragmentNewPlaylist || destination.id == R.id.playlistScreenFragment){
                true -> bottomNavigationView.visibility = View.GONE
                false -> bottomNavigationView.visibility = View.VISIBLE
            }

        }
        bottomNavigationView.setupWithNavController(navController)
    }



}
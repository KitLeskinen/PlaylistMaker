package com.practicum.playlistmaker.presentation.model.ui.search

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.Consumer
import com.practicum.playlistmaker.domain.api.ConsumerData
import com.practicum.playlistmaker.domain.entity.Track
import com.practicum.playlistmaker.domain.entity.TrackResponse
import com.practicum.playlistmaker.presentation.state.SearchState

class SearchViewModel(private val application: Application) : ViewModel() {



    companion object {
        fun factory(
            application: Application
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel(application)
                }
            }
        }
    }

}
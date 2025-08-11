package com.practicum.playlistmaker.medialibrary.ui

import com.practicum.playlistmaker.common.data.domain.entity.Playlist


sealed interface PlaylistScreenState {
    data class Loading(val playlist: Playlist) : PlaylistScreenState
}
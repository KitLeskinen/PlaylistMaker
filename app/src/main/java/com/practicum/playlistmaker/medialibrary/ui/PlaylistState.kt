package com.practicum.playlistmaker.medialibrary.ui

import com.practicum.playlistmaker.common.data.domain.entity.Playlist


sealed interface PlaylistState {
    data class Loading(val playlists: List<Playlist>) : PlaylistState
}
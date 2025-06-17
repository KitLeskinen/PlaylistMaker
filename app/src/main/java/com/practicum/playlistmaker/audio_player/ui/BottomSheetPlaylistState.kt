package com.practicum.playlistmaker.audio_player.ui

import com.practicum.playlistmaker.common.data.domain.entity.Playlist


sealed interface BottomSheetPlaylistState {
    data class Loading(val playlists: List<Playlist>) : BottomSheetPlaylistState
}
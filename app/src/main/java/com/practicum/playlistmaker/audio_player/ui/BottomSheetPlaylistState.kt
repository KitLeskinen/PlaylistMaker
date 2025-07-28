package com.practicum.playlistmaker.audio_player.ui

import com.practicum.playlistmaker.common.data.domain.entity.Playlist


sealed interface BottomSheetPlaylistState {
    data class Loading(val playlists: List<Playlist>) : BottomSheetPlaylistState
    data class TrackAdded(val result: Long, val name: String, val playlists: List<Playlist>?): BottomSheetPlaylistState
}
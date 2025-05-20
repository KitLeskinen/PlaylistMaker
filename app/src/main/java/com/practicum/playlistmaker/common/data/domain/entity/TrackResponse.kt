package com.practicum.playlistmaker.common.data.domain.entity

data class TrackResponse (
    val trackList: List<Track>,
    val isError: Boolean,
    val errorMessage: String
)
package com.practicum.playlistmaker.domain.entity

data class TrackResponse (
    val trackList: List<Track>,
    val isError: Boolean,
    val errorMessage: String
)
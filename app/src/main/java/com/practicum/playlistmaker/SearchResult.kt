package com.practicum.playlistmaker

import com.google.gson.annotations.SerializedName

data class SearchResult (
    val resultCount: Int,
    @SerializedName("results") val tracks: List<Track>
)
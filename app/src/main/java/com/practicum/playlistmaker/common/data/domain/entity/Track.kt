package com.practicum.playlistmaker.common.data.domain.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    @SerializedName("trackId") val trackId: Long,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    @SerializedName("country") val country: String,
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("TrackInteractor.kt") val previewUrl: String,
) : Serializable{
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}


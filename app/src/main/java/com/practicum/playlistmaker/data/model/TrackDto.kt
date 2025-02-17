package com.practicum.playlistmaker.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TrackDto (

    @SerializedName("trackId") val trackId: Long,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    @SerializedName("country") val country: String,
    @SerializedName("primaryGenreName") val primaryGenreName: String,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("collectionName") val collectionName: String,
    @SerializedName("previewUrl") val previewUrl: String
    ) : Serializable
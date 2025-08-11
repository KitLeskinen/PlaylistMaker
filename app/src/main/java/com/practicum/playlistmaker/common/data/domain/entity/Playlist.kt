package com.practicum.playlistmaker.common.data.domain.entity

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Playlist(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("coverUri") val coverUri: Uri?,
    @SerializedName("trackList") val trackList: MutableList<Track>?
) : Serializable

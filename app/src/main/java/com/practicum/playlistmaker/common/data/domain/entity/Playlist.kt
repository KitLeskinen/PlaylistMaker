package com.practicum.playlistmaker.common.data.domain.entity

import android.net.Uri

data class Playlist (val name: String,
                     val description: String,
                     val coverUri: Uri?,
                     val trackList: MutableList<Track>?)

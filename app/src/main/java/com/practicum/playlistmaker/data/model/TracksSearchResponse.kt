package com.practicum.playlistmaker.data.model



class TracksSearchResponse(
    val results: List<TrackDto>
) : NetResponse()
package com.practicum.playlistmaker.search.data.model



class TracksSearchResponse(
    val results: List<TrackDto>
) : NetResponse()
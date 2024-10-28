package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val APP_PREFERENCES = "app_preferences"
const val SEARCH_HISTORY_KEY = "search_history_key"

class SearchHistory (
    private val sharedPreferences: SharedPreferences
){
    private val gson = Gson()

    // About generic type and gson: https://github.com/google/gson/blob/main/UserGuide.md#serializing-and-deserializing-generic-types

    fun getTracksHistory() : MutableList<Track>{

        val tracksJSON = sharedPreferences.getString(SEARCH_HISTORY_KEY, "[]")

        val type = object : TypeToken<MutableList<Track>>() {}.type

        val tracksList: MutableList<Track> = gson.fromJson(tracksJSON, type)
        return tracksList
    }
    fun saveTracksHistory(mutableList: MutableList<Track>){
        val gson = Gson()
        val type = object  : TypeToken<MutableList<Track>>() {}.type
        val tracksJSON = gson.toJson(mutableList, type)
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, tracksJSON).apply()
    }
    fun clearHistory(){
        sharedPreferences.edit().clear().apply()
    }
}
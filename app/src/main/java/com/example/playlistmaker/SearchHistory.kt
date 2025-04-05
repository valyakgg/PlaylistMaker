package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val KEY_HISTORY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val gson = Gson()

    fun getHistory(): List<Track> {
        val json = sharedPrefs.getString(KEY_HISTORY, null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()
        history.removeAll { it.trackName == track.trackName && it.artistName == track.artistName }
        history.add(0, track)
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.lastIndex)
        }
        saveHistory(history)
    }

    fun clearHistory() {
        sharedPrefs.edit().remove(KEY_HISTORY).apply()
    }

    private fun saveHistory(history: List<Track>) {
        val json = gson.toJson(history)
        sharedPrefs.edit().putString(KEY_HISTORY, json).apply()
    }
}

package com.example.testassignment.utils

import android.content.Context
import android.content.SharedPreferences

object StatsStorage {
    private const val PREFS_NAME = "frame_stats"
    private const val KEY_FRAME_COUNT = "frame_count"
    private const val KEY_BYTES_SENT = "bytes_sent"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun incrementFrameCount(context: Context) {
        val prefs = getPrefs(context)
        val current = prefs.getInt(KEY_FRAME_COUNT, 0)
        prefs.edit().putInt(KEY_FRAME_COUNT, current + 1).apply()
    }

    fun addBytesSent(context: Context, bytes: Int) {
        val prefs = getPrefs(context)
        val current = prefs.getLong(KEY_BYTES_SENT, 0L)
        prefs.edit().putLong(KEY_BYTES_SENT, current + bytes).apply()
    }

    fun getFrameCount(context: Context): Int {
        return getPrefs(context).getInt(KEY_FRAME_COUNT, 0)
    }

    fun getBytesSent(context: Context): Long {
        return getPrefs(context).getLong(KEY_BYTES_SENT, 0L)
    }

    fun resetStats(context: Context) {
        getPrefs(context).edit()
            .putInt(KEY_FRAME_COUNT, 0)
            .putLong(KEY_BYTES_SENT, 0L)
            .apply()
    }
}
package com.fingerspell.tutor.data.local

import android.content.Context
import android.content.SharedPreferences

class TutorPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("fingerspell_tutor", Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putBool(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun text(key: String): String = prefs.getString(key, "") ?: ""

    fun putText(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun wipe() {
        prefs.edit().clear().apply()
    }

    fun decodeCounts(raw: String): Map<String, Int> {
        if (raw.isBlank()) return emptyMap()
        val out = LinkedHashMap<String, Int>()
        raw.split('|').forEach { chunk ->
            val parts = chunk.split(':')
            if (parts.size == 2) {
                val value = parts[1].toIntOrNull()
                if (value != null && parts[0].isNotEmpty()) out[parts[0]] = value
            }
        }
        return out
    }

    fun encodeCounts(values: Map<String, Int>): String =
        values.entries.joinToString("|") { it.key + ":" + it.value }

    fun decodeIds(raw: String): Set<Int> {
        if (raw.isBlank()) return emptySet()
        return raw.split('|').mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun encodeIds(values: Set<Int>): String = values.joinToString("|")
}

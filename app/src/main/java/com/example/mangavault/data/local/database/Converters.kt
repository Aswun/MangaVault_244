package com.example.mangavault.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Kelas Konverter untuk Room Database.
 * Digunakan untuk mengubah tipe data kompleks (seperti List) menjadi format yang bisa disimpan di SQLite (JSON String).
 */
class Converters {

    /**
     * Mengubah String JSON kembali menjadi List<String>.
     */
    @TypeConverter
    fun fromStringList(value: String?): List<String>? {
        if (value == null) return null
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    /**
     * Mengubah List<String> menjadi format String JSON untuk disimpan.
     */
    @TypeConverter
    fun toStringList(list: List<String>?): String? {
        return Gson().toJson(list)
    }
}
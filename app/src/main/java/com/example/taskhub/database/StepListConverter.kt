package com.example.taskhub.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StepListConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToList(value: String): List<Step> {
        return if (value.isEmpty()) {
            listOf()
        } else {
            val type = object : TypeToken<List<Step>>() {}.type
            gson.fromJson(value, type)
        }
    }

    @TypeConverter
    fun listToString(value: List<Step>): String? {
        return if (value.isEmpty()) {
            null
        } else {
            gson.toJson(value)
        }
    }

}
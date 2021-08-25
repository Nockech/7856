package com.example.taskhub.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskListConverter {
    private val gson = Gson()

    @TypeConverter
    fun stringToList(value: String): List<Task> {
        return if (value.isEmpty()) {
            listOf()
        } else {
            val type = object : TypeToken<List<Task>>() {}.type
            gson.fromJson(value, type)
        }
    }

    @TypeConverter
    fun listToString(value: List<Task>): String? {
        return if (value.isEmpty()) {
            null
        } else {
            gson.toJson(value)
        }
    }

}
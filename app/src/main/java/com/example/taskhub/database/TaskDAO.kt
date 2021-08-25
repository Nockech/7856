package com.example.taskhub.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(Task: Task) : Long

    @Update
    suspend fun updateTask(Task: Task) : Int

    @Delete
    suspend fun deleteTask(Task: Task) : Int

    @Query("DELETE FROM task_data_table")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM task_data_table")
    fun getAllTasks() : LiveData<List<Task>>
}
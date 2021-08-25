package com.example.taskhub.view

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhub.database.Task
import com.example.taskhub.database.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel(), Observable {
    val tasks = repository.tasks
    var taskCount: Int = 0
        get() { return tasks.value?.size ?: 0 }
        private set

    //basic repository methods
    fun delete(task: Task) = viewModelScope.launch {
        val noOfRowsDeleted = repository.delete(task)
        /*
        if (noOfRowsDeleted <= 0){
            throw Exception("Error on DayVM delete function")
        }*/
    }
    fun update(task: Task) = viewModelScope.launch {
        val noOfRows = repository.update(task)
        /*
        if (noOfRows <= 0){
            throw Exception("Error on DayVM update function")
        }*/
    }
    fun insert(task: Task) = viewModelScope.launch {
        val newRowId = repository.insert(task)
        if (newRowId <= -1)
            throw Exception("Error on DayVM update function")
    }
    fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted = repository.deleteAll()
        /*
        if (noOfRowsDeleted <= 0) {
            //throw Exception("Error on clearAll function")
        }*/
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) { }
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) { }
}
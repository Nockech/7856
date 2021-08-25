package com.example.taskhub.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskhub.AddTaskFragment
import com.example.taskhub.database.TaskRepository

@Suppress("UNCHECKED_CAST")
class TaskViewModelFactory(private val repository: TaskRepository, private val addTaskFragment : AddTaskFragment? = null) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java)){
            return (TaskViewModel(repository) as T)
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
package com.example.taskhub.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskhub.AddTaskFragment

@Suppress("UNCHECKED_CAST")
class AddTaskFragmentVMFactory(val taskViewModel: TaskViewModel, val addTaskFragment : AddTaskFragment): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddTaskFragmentViewModel::class.java)){
            return (AddTaskFragmentViewModel(taskViewModel, addTaskFragment) as T)
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}
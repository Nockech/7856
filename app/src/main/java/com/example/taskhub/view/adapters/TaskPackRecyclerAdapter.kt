package com.example.taskhub.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.R
import com.example.taskhub.database.Task
import com.example.taskhub.databinding.TaskPackItemBinding
import com.example.taskhub.view.TaskViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TaskPackRecyclerAdapter(private val fragment: Fragment, private val viewModel: TaskViewModel) :
    RecyclerView.Adapter<TaskPackViewHolder>() {

    private val taskList = ArrayList<List<Task>>()
    var onEmptyTrigger: (Int) -> Unit = {}

    fun getTaskList(): ArrayList<List<Task>> { return taskList }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskPackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TaskPackItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.task_pack_item, parent, false)
        onEmptyTrigger(taskList.size)
        return TaskPackViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskPackViewHolder, position: Int) {
        val adapter = TaskRecyclerViewAdapter(fragment, viewModel)
        adapter.setList(taskList[position])
        holder.bind(adapter)
    }

    fun setList(sortedTasks: List<List<Task>>){
        taskList.clear()
        taskList.addAll(sortedTasks)

        onEmptyTrigger(taskList.size)
    }
}

@SuppressLint("NotifyDataSetChanged")
class TaskPackViewHolder(private val binding: TaskPackItemBinding, private val parentAdapter: TaskPackRecyclerAdapter) : RecyclerView.ViewHolder(binding.root){
    fun bind(taskAdapter: TaskRecyclerViewAdapter){
        //TODO: impl deez times ha-ha-ha
        binding.taskPackDate.text = taskAdapter.getTaskList()[0].date

        binding.taskRecycler.adapter = taskAdapter
        binding.taskRecycler.layoutManager = LinearLayoutManager(taskAdapter.fragment.context)

        binding.taskPackDelete.setOnClickListener {
            MaterialAlertDialogBuilder(taskAdapter.fragment.requireContext(), R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Delete tasks?")
                .setMessage("All tasks for ${binding.taskPackDate.text} will be deleted forever, are you sure?")
                .setPositiveButton("Delete"){ _, _ ->
                    for (i in taskAdapter.getTaskList()){
                        taskAdapter.getViewModel().delete(i)
                    }
                    binding.taskRecycler.alpha = 0.5F
                    binding.taskPackDelete.setOnClickListener {  }
                    parentAdapter.notifyDataSetChanged()
                }
                .setNegativeButton("Cancel"){ _, _ -> }
                .show()

        }


    }
}
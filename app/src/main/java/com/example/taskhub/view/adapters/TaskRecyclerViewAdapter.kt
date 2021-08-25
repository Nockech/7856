package com.example.taskhub.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.R
import com.example.taskhub.database.Task
import com.example.taskhub.databinding.TaskItemBinding
import com.example.taskhub.extensions.fastToast
import com.example.taskhub.view.TaskViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TaskRecyclerViewAdapter(val fragment: Fragment, private val viewModel: TaskViewModel) :
    RecyclerView.Adapter<TaskViewHolder>() {

    private val taskList = ArrayList<Task>()
    var onEmptyTrigger: (Int) -> Unit = {}

    fun getViewModel(): TaskViewModel { return viewModel }
    fun getTaskList(): ArrayList<Task> { return taskList }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : TaskItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.task_item, parent,false)

        onEmptyTrigger(taskList.size)
        return TaskViewHolder(binding, this)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position], fragment, viewModel)
    }

    fun setList(tasks: List<Task>){
        taskList.clear()
        taskList.addAll(tasks)

        onEmptyTrigger(taskList.size)
    }

}
@SuppressLint("NotifyDataSetChanged")
class TaskViewHolder(private val binding: TaskItemBinding, private val parentAdapter: TaskRecyclerViewAdapter) : RecyclerView.ViewHolder(binding.root){
    @SuppressLint("SetTextI18n")
    fun bind(task: Task, fragment: Fragment, viewModel: TaskViewModel){
        binding.titleTextView.text = task.title

        val dateText = if (task.time.isNotEmpty()) "${task.date}  /  ${task.time}" else task.date
        binding.dateTextView.text = dateText

        val stepAdapter = StepRecyclerViewAdapter()
        binding.taskStepRecycler.layoutManager = LinearLayoutManager(fragment.context)
        binding.taskStepRecycler.adapter = stepAdapter

        binding.taskShowContentButton.setOnClickListener {
            toggleContent(stepAdapter, task)
        }

        //TODO: implement deez

        binding.listItemLayout.setOnLongClickListener {
            try{
                callDeleteAlert(fragment.requireContext(), viewModel, task)
            }
            catch(e: Exception) {
                fragment.requireActivity().fastToast("Task already deleted")
            }

            parentAdapter.notifyDataSetChanged()
            return@setOnLongClickListener true
        }

        stepAdapter.updateParentTask = { viewModel.update(task) }
        stepAdapter.setList(task.steps)
        stepAdapter.notifyDataSetChanged()

        toggleContent(stepAdapter, task)
    }

    private fun toggleContent(adapter: StepRecyclerViewAdapter, task: Task){
        if(adapter.getList().isNullOrEmpty()){
            adapter.setList(task.steps)
        }else{
            adapter.setList(ArrayList())
        }
        adapter.notifyDataSetChanged()
    }
    private fun callDeleteAlert(context: Context, viewModel: TaskViewModel, task: Task){
        MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Delete task?")
            .setMessage("Task \"${task.title}\" will be deleted forever")
            .setPositiveButton("Delete"){ _, _ ->
                viewModel.delete(task)
                parentAdapter.notifyDataSetChanged()
                binding.demiTransparentLayout.alpha = 0.5F
            }
            .setNegativeButton("Cancel"){ _, _ -> }
            .show()
    }
}
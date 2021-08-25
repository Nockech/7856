package com.example.taskhub.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.R
import com.example.taskhub.database.Step
import com.example.taskhub.databinding.StepItemBinding

class StepRecyclerViewAdapter :
    RecyclerView.Adapter<StepViewHolder>() {

    private var stepList = ArrayList<Step>()
    var onEmptyTrigger: (Int) -> Unit = {}
    var updateParentTask: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : StepItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.step_item, parent, false)
        onEmptyTrigger(itemCount)
        return StepViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return stepList.size
    }
    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(stepList[position], updateParentTask)
    }


    fun insertStep(step: Step){
        stepList.add(step)

        notifyDataSetChanged()
        onEmptyTrigger(itemCount)
    }
    fun setList(tasks: List<Step>){
        stepList.clear()
        stepList.addAll(tasks)

        onEmptyTrigger(itemCount)
    }
    fun getList() : List<Step>{
        return stepList
    }
    fun deleteLast(){
        if (!stepList.isNullOrEmpty()){
            stepList.remove(stepList.last())

            notifyDataSetChanged()
            onEmptyTrigger(itemCount)
        }
    }
    fun clearAll(){
        stepList = ArrayList()

        notifyDataSetChanged()
        onEmptyTrigger(itemCount)
    }
}

class StepViewHolder(private val binding: StepItemBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(step: Step, updateParentTask: () -> Unit){
        binding.stepTextView.text = step.text
        binding.stepCheckBox.isChecked = step.isCompleted

        //look in side of TaskViewModel
        binding.stepCheckBox.setOnClickListener {
            step.isCompleted = binding.stepCheckBox.isChecked//bruh, it's actually worked. Finally, knowledge is the path to singularity
            updateParentTask()
        }
    }
}
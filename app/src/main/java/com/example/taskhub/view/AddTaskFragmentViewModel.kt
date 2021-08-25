package com.example.taskhub.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskhub.AddTaskFragment
import com.example.taskhub.MainActivity
import com.example.taskhub.R
import com.example.taskhub.utils.TimeManager
import com.example.taskhub.database.Step
import com.example.taskhub.database.Task
import com.example.taskhub.utils.NotifyService
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

@SuppressLint("NotifyDataSetChanged")
class AddTaskFragmentViewModel(val taskViewModel: TaskViewModel, private val addTaskFragment : AddTaskFragment): ViewModel(), Observable {
    var stepAdapter = addTaskFragment.stepAdapter

    private val inputTime = MutableLiveData<String>()

    @Bindable
    val inputTitle = MutableLiveData<String>()
    @Bindable
    val inputDate = MutableLiveData<String>()
    @Bindable
    val inputStep = MutableLiveData<String>()

    @Bindable
    val pickDateBtnText = MutableLiveData("pick date")
    @Bindable
    val pickTimeBtnText = MutableLiveData("set time")

    fun insertWithAdapter(){
        val task = inputTitle.value?.let {
            inputDate.value?.let { date ->
                Task(0,
                    it,
                    date,
                    stepAdapter.getList(),
                    if (inputTime.value != null)  "${inputTime.value}" else ""
                )
            }
        }

        if (inputTitle.value.isNullOrBlank()){
            addTaskFragment.reply("Task missing tittle field")

        }else if(stepAdapter.getList().isNullOrEmpty()){
            addTaskFragment.reply("Undefined steps for this task")

        }else if(inputDate.value.isNullOrBlank()){
            addTaskFragment.reply("Undefined task completion date")

        }else if (task != null){
            taskViewModel.insert(task)
            addTaskFragment.reply("Saved")

            for(i in addTaskFragment.notifyAdapter.getList()){
                NotifyService.setNotification(addTaskFragment.requireContext(), task, i)
            }

            inputTitle.value = ""
            inputStep.value = ""
            inputDate.value = ""
            inputTime.value = ""
            (addTaskFragment.activity as MainActivity)
            addTaskFragment.notifyAdapter.setList(listOf())
            addTaskFragment.notifyAdapter.notifyDataSetChanged()
            stepAdapter.clearAll()
        }
    }


    fun callDatePicker(){
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .setTitleText("Select task completion date")
            .build()

        datePicker.addOnPositiveButtonClickListener {
            val date = TimeManager.getParsedDate(it)

            inputDate.value = "${date[0]}.${date[1]}.${date[2]}"
            pickDateBtnText.value = "${date[0]}.${date[1]}.${date[2]}"

            addTaskFragment.reply("Set date on ${date[0]}.${date[1]}.${date[2]}")

            callTimeAsk(addTaskFragment.requireContext())
        }

        addTaskFragment.activity?.let {
            datePicker.show(it.supportFragmentManager,"DATE_PICKER")
        }
    }


    private fun callTimeAsk(context: Context){
        MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Set time for this task?")
            .setMessage("Set up completion time and notifications for this task")
            .setPositiveButton("Set time"){ _, _ ->
                callTimePicker()
            }
            .setNegativeButton("Cancel"){ _, _ -> }
            .show()
    }
    fun callTimePicker(){
        if (!inputDate.value.isNullOrBlank()){
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val time = "${timePicker.hour}:${ 
                    if(timePicker.minute.toString().length == 1) "00" 
                    else timePicker.minute
                }"

                inputTime.value = time
                pickTimeBtnText.value = time

                addTaskFragment.reply("Set time on $time")

                callNotifyAsk(addTaskFragment.requireContext())
            }

            timePicker.show(addTaskFragment.requireActivity().supportFragmentManager,"DATE_PICKER")
        }
        else
            addTaskFragment.reply("Pick date first")

    }


    private fun callNotifyAsk(context: Context){
        MaterialAlertDialogBuilder(context, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
            .setTitle("Set up Notifications?")
            .setMessage("Set up notifications before task completion time?")
            .setPositiveButton("Set notifications"){ _, _ ->
                callNotifyEdit()
            }
            .setNegativeButton("Cancel"){ _, _ -> }
            .show()
    }
    fun callNotifyEdit(){
        if (!inputTime.value.isNullOrBlank()){
            if(addTaskFragment.notifyAlertView.parent != null){
                (addTaskFragment.notifyAlertView.parent as ViewGroup).removeAllViews()
            }

            addTaskFragment.replaceNotifyOnPress()
            addTaskFragment.notifyAlert.show()
        }
        else
            addTaskFragment.reply("Set time first")
    }


    fun adapterAddStep(){
        val step = inputStep.value?.let {
            Step(it, false)
        }

        if ( step != null && !inputStep.value.isNullOrBlank() ) {
            inputStep.value = ""
            stepAdapter.insertStep(step)
        }
        else{
            addTaskFragment.reply("Step missing text")
        }
    }
    fun adapterDeleteLast(){
        stepAdapter.deleteLast()
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}
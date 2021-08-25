package com.example.taskhub

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.database.TaskDatabase
import com.example.taskhub.database.TaskRepository
import com.example.taskhub.databinding.FragmentAddTaskBinding
import com.example.taskhub.view.*
import com.example.taskhub.view.adapters.NotifyRecyclerViewAdapter
import com.example.taskhub.view.adapters.StepRecyclerViewAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var addFragmentViewModel: AddTaskFragmentViewModel

    lateinit var stepAdapter: StepRecyclerViewAdapter
        private set
    lateinit var emptyRecyclerText: TextView
        private set

    lateinit var notifyAdapter: NotifyRecyclerViewAdapter
        private set
    lateinit var notifyAlertView: View
        private set
    lateinit var notifyAlert: MaterialAlertDialogBuilder
        private set

    lateinit var stepEditText: EditText
        private set


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = TaskDatabase.getInstance(requireActivity().application).taskDAO
        val repository = TaskRepository(dao)
        val factory = AddTaskFragmentVMFactory(TaskViewModel(repository), this)

        initRecyclerView()

        addFragmentViewModel = ViewModelProvider(this, factory).get(AddTaskFragmentViewModel::class.java)
        emptyRecyclerText = view.findViewById(R.id.empty_recycler_text)
        stepEditText = view.findViewById(R.id.stepEditText)

        binding.taskViewModel = addFragmentViewModel
        binding.lifecycleOwner = this

        initNotifyRecycler()
        initNotifyAlert()
        setStepEditOnEnter()
    }


    private fun initRecyclerView(){
        stepAdapter = StepRecyclerViewAdapter()
        stepAdapter.onEmptyTrigger = { arg: Int -> emptyRecyclerCheck(arg) }

        binding.stepRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.stepRecyclerView.adapter = stepAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initNotifyRecycler(){
        notifyAdapter = NotifyRecyclerViewAdapter()
        notifyAdapter.onLongPressTrigger = { addFragmentViewModel.callNotifyEdit() }
        binding.notifyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.notifyRecyclerView.adapter = notifyAdapter
    }

    private fun initNotifyAlert(){
        val inflater: LayoutInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        notifyAlertView = inflater.inflate(R.layout.edit_notify_alert, null)

        val alertRecycler = notifyAlertView.findViewById<RecyclerView>(R.id.notify_alert_recycler)
        alertRecycler.adapter = notifyAdapter
        alertRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val addNewButton = notifyAlertView.findViewById<Button>(R.id.alert_notify_add_new_button)
        addNewButton.setOnClickListener {
            val hrsEditText = notifyAlertView.findViewById<EditText>(R.id.notify_hours_edit)
            val minEditText = notifyAlertView.findViewById<EditText>(R.id.notify_minutes_edit)

            if (getNotifyDelay(hrsEditText, minEditText) !in listOf("0:00", "0:0"))
                notifyAdapter.insertNotify(getNotifyDelay(hrsEditText, minEditText))

            hrsEditText.text.clear()
            minEditText.text.clear()
        }

        val deleteButton = notifyAlertView.findViewById<Button>(R.id.alert_notify_delete_button)
        deleteButton.setOnClickListener {
            notifyAdapter.deleteLast()
        }

        notifyAlert = context?.let {
            MaterialAlertDialogBuilder(it, R.style.MaterialEditNotifyAlert)
                .setTitle("Edit Notifications")
                .setMessage("Set a delay before task completion time to notify you of a deadline:")
                .setOnDismissListener { returnNotifyOnPress() }
                .setView(notifyAlertView)
        }!!
    }

    private fun setStepEditOnEnter(){
        stepEditText.setOnEditorActionListener { _, _, _ ->
            addFragmentViewModel.adapterAddStep()
            true
        }
    }

    private fun returnNotifyOnPress(){
        notifyAdapter.onLongPressTrigger = { addFragmentViewModel.callNotifyEdit() }
    }

    fun replaceNotifyOnPress(){
        notifyAdapter.onLongPressTrigger = {}
    }

    private fun getNotifyDelay(hoursText: EditText, minutesText: EditText): String{
        val hours = if (hoursText.text.toString() == "") "0" else hoursText.text.toString()
        val minutes = if (minutesText.text.toString() == "") "00" else minutesText.text.toString()

        return "${hours}:${minutes}"
    }

    fun reply(text: String){
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
    }

    private fun emptyRecyclerCheck(count: Int){
        if (count == 0){
            emptyRecyclerText.visibility = View.VISIBLE
        }
        else{
            emptyRecyclerText.visibility = View.INVISIBLE
        }
    }
}
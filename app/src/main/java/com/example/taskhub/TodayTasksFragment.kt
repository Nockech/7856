package com.example.taskhub

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.utils.TimeManager
import com.example.taskhub.utils.TaskSorter
import com.example.taskhub.database.TaskDatabase
import com.example.taskhub.database.TaskRepository
import com.example.taskhub.view.adapters.TaskRecyclerViewAdapter
import com.example.taskhub.view.TaskViewModel
import java.io.Serializable


class TodayTasksFragment : Serializable, Fragment(R.layout.fragment_today_tasks) {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TaskRecyclerViewAdapter
    lateinit var taskViewModel: TaskViewModel
    lateinit var recyclerText: TextView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_today_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = TaskDatabase.getInstance(requireActivity().application).taskDAO
        val repository = TaskRepository(dao)

        taskViewModel = TaskViewModel(repository)
        recyclerView = view.findViewById(R.id.today_tasks_recycler_view)
        recyclerText = view.findViewById(R.id.today_empty_recycler_text)

        initRecyclerView()
    }

    private fun initRecyclerView(){
        adapter = TaskRecyclerViewAdapter(this, taskViewModel)
        adapter.onEmptyTrigger = { arg: Int -> emptyRecyclerCheck(arg) }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        displayTasks()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun displayTasks(){
        taskViewModel.tasks.observe(viewLifecycleOwner, { tasks ->
            val packs = TaskSorter.getDateSorted(tasks)
            val todayTasks = packs.filter { pack -> pack[0].date == TimeManager.getNowDate() }

            //Warning! content refreshing based on adapter's and database's lists SIZE comparison!
            if (!todayTasks.isNullOrEmpty()){
                if (todayTasks.size != adapter.getTaskList().size){
                    adapter.setList(todayTasks[0])
                    adapter.notifyDataSetChanged()
                }
            }

        })
    }

    private fun emptyRecyclerCheck(count: Int){
        activity?.runOnUiThread {
            if (count == 0){
                recyclerText.visibility = View.VISIBLE
            } else{
                recyclerText.visibility = View.INVISIBLE
            }
        }
    }
}
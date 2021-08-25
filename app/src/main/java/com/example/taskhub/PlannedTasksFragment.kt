package com.example.taskhub

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.utils.TaskSorter
import com.example.taskhub.database.TaskDatabase
import com.example.taskhub.database.TaskRepository
import com.example.taskhub.view.adapters.TaskPackRecyclerAdapter
import com.example.taskhub.view.TaskViewModel
import java.util.*


class PlannedTasksFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerText: TextView
    lateinit var adapter: TaskPackRecyclerAdapter
    lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_planned_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dao = TaskDatabase.getInstance(requireActivity().application).taskDAO
        val repository = TaskRepository(dao)

        taskViewModel = TaskViewModel(repository)
        recyclerView = view.findViewById(R.id.planned_tasks_recycler_view)
        recyclerText = view.findViewById(R.id.planned_empty_text)

        initRecyclerView()
    }


    private fun initRecyclerView(){
        adapter = TaskPackRecyclerAdapter(this, taskViewModel)
        adapter.onEmptyTrigger = { arg: Int -> emptyRecyclerCheck(arg) }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        displayTasks()

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun displayTasks(){
        taskViewModel.tasks.observe(viewLifecycleOwner, {
            //Warning! content refreshing based on adapter's and database's lists comparison!
            val content = TaskSorter.getDateSorted(Collections.unmodifiableList(it))
            if (content != adapter.getTaskList())
            {
                adapter.setList(content)
                adapter.notifyDataSetChanged()
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
package com.example.taskhub.utils

import com.example.taskhub.database.Task

class TaskSorter {
    companion object{
        fun getDateSorted(list: List<Task>): List<List<Task>>{
            val arr = list.sortedWith { f, s ->
                val fdraw = f.date.split('.')
                val sdraw = s.date.split('.')

                val fd = "${fdraw[2]}${fdraw[1]}${fdraw[0]}".toInt()
                val sd = "${sdraw[2]}${sdraw[1]}${sdraw[0]}".toInt()

                when {
                    fd > sd -> 1
                    fd < sd -> -1
                    else -> 0
                }
            }

            val out = mutableListOf<List<Task>>()
            val temp = mutableListOf<Task>()

            for (i in arr.indices){
                if (temp.isEmpty()){
                    temp.add(arr[i])
                }else{
                    if (temp[0].date != arr[i].date){
                        val clone = mutableListOf<Task>()
                        clone.addAll(temp)

                        out.add(clone)
                        temp.clear()
                    }
                    temp.add(arr[i])
                }
            }

            if(temp.isNotEmpty())
                out.add(temp)

            return out
        }


    }
}
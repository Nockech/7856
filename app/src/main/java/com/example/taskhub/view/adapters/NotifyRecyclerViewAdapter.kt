package com.example.taskhub.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskhub.R
import com.example.taskhub.databinding.NotifyItemBinding

class NotifyRecyclerViewAdapter:
    RecyclerView.Adapter<NotifyViewHolder>(){

    private var notifyDelays = ArrayList<String>()
    var onEmptyTrigger: (Int) -> Unit = {}
    var onLongPressTrigger: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : NotifyItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.notify_item, parent, false)
        onEmptyTrigger(itemCount)
        return NotifyViewHolder(binding, onLongPressTrigger)
    }
    override fun getItemCount(): Int {
        return notifyDelays.size
    }
    override fun onBindViewHolder(holder: NotifyViewHolder, position: Int) {
        holder.bind(notifyDelays[position])
    }

    fun insertNotify(delay: String){
        notifyDelays.add(delay)

        notifyDataSetChanged()
        onEmptyTrigger(itemCount)
    }
    fun setList(delays: List<String>){
        notifyDelays.clear()
        notifyDelays.addAll(delays)

        onEmptyTrigger(itemCount)
    }
    fun getList() : List<String>{
        return notifyDelays
    }
    fun deleteLast(){
        if (!notifyDelays.isNullOrEmpty()){
            notifyDelays.remove(notifyDelays.last())

            notifyDataSetChanged()
            onEmptyTrigger(itemCount)
        }
    }
    fun clearAll(){
        notifyDelays = ArrayList()

        notifyDataSetChanged()
        onEmptyTrigger(itemCount)
    }
}

class NotifyViewHolder(private val binding: NotifyItemBinding, private val onLongPress: () -> Unit) : RecyclerView.ViewHolder(binding.root){
    fun bind(notifyDelay: String){
        binding.notifyTime.text = notifyDelay
        binding.notifyItemCard.setOnLongClickListener {
            onLongPress()
            return@setOnLongClickListener true
        }
    }
}
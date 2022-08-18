package com.example.todo_app.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.data.entities.Task
import com.example.todo_app.databinding.RowItemTaskBinding
import com.example.todo_app.recyclerFeatures.ItemMoveCallback
import java.util.*
import kotlin.collections.ArrayList


class TaskAdapter(
    private val changeSituation: (Task) -> Unit,
    private val update: (Task) -> Unit,
    private val updateFunc: (Task, Boolean) -> Unit
): ListAdapter <Task, TaskViewHolder>(DiffCallback()), ItemMoveCallback.ItemTouchHelperContract {
    private var isNight: Boolean = false

    fun setLightMode(isNight: Boolean){
        this.isNight = isNight
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:RowItemTaskBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_item_task, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(isNight, getItem(position), changeSituation, update)
    }


    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                swapPosition(i, i+1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                swapPosition(i, i-1)
            }
        }
        // notifyItemMoved(fromPosition, toPosition)
    }


    private fun swapPosition(from: Int, to: Int){
        val newList = currentList.toMutableList()
        val temp = newList[from].position
        newList[from].position = newList[to].position
        newList[to].position = temp
        Collections.swap(newList, from, to)
        submitList(newList)
    }


    override fun submitList(list: List<Task>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    override fun onRowSelected(myViewHolder: TaskViewHolder?) {
       myViewHolder!!.setColorBackground((0xFFF5F5F5).toInt())
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onRowClear(myViewHolder: TaskViewHolder?) {

       for (task in currentList.toMutableList()){
            updateFunc(task, false)
       }

        notifyDataSetChanged()
        myViewHolder!!.setBackground(R.drawable.costum_item_background)
    }
}


class TaskViewHolder(private val binding: RowItemTaskBinding): RecyclerView.ViewHolder(binding.root){
 fun bind(isNight: Boolean, task: Task, changeSituation: (Task) -> Unit, update: (Task) -> Unit){
     binding.checkBox.isChecked = task.situation
     binding.tvText.text = task.text
     if (task.situation){
         if (isNight){
             binding.tvText.setTextColor((0xFF777A92).toInt())
         }else{
             binding.tvText.setTextColor((0xFFd8d7d6).toInt())
         }
         binding.tvText.paintFlags = binding.tvText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
     }else{
         if (isNight){
             binding.tvText.setTextColor((0xFFE4E5F1).toInt())
         }else{
             binding.tvText.setTextColor((0xFF000000).toInt())
         }
         binding.tvText.paintFlags = binding.tvText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
     }

     binding.ibDelete.setOnClickListener {
         changeSituation(task)
     }
     binding.checkBox.setOnClickListener {
         update(task)
     }
 }
    fun setBackground(color: Int){
        binding.itemCardView.setBackgroundResource(color)
    }
    fun setColorBackground(color: Int){
        binding.itemCardView.setBackgroundColor(color)
    }

}


class DiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

}
package com.example.todo_app

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.data.Task
import com.example.todo_app.databinding.RowItemTaskBinding

class TaskAdapter(
    private val isNight: Boolean,
    private val tasks: List<Task>,
    private val function: (Task) -> Unit,
    private val update: (Task) -> Unit
): RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:RowItemTaskBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_item_task, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(isNight, tasks[position], function, update)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
class TaskViewHolder(private val binding: RowItemTaskBinding): RecyclerView.ViewHolder(binding.root){
 fun bind(isNight: Boolean, task: Task, function: (Task) -> Unit, update: (Task) -> Unit){
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
             binding.tvText.setTextColor((0xFFEAF6F6git).toInt())
         }else{
             binding.tvText.setTextColor((0xFF000000).toInt())
         }
         binding.tvText.paintFlags = binding.tvText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
     }

     binding.ibDelete.setOnClickListener {
         function(task)
     }
     binding.checkBox.setOnClickListener {
         update(task)
     }
 }
}
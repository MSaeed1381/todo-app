package com.example.todo_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.data.Task
import com.example.todo_app.databinding.RowItemTaskBinding

class TaskAdapter(val tasks: List<Task>): RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:RowItemTaskBinding = DataBindingUtil.inflate(layoutInflater, R.layout.row_item_task, parent, false)
        return TaskViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
class TaskViewHolder(private val binding: RowItemTaskBinding): RecyclerView.ViewHolder(binding.root){
 fun bind(task: Task){
     binding.checkBox.isChecked = task.situation
     binding.tvText.text = task.text
 }
}
package com.example.todo_app

import android.content.res.ColorStateList
import android.graphics.ColorSpace
import android.graphics.Paint
import android.provider.CalendarContract
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.data.Task
import com.example.todo_app.databinding.RowItemTaskBinding

class TaskAdapter(
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
        holder.bind(tasks[position], function, update)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
class TaskViewHolder(private val binding: RowItemTaskBinding): RecyclerView.ViewHolder(binding.root){
 fun bind(task: Task, function: (Task) -> Unit, update: (Task) -> Unit){
     binding.checkBox.isChecked = task.situation
     binding.tvText.text = task.text
     if (task.situation){
         binding.tvText.paintFlags = binding.tvText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
         binding.tvText.setTextColor((0xFFd8d7d6).toInt())
     }else{
         binding.tvText.paintFlags = binding.tvText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
         binding.tvText.setTextColor((0xff000000).toInt())
     }


     binding.ibDelete.setOnClickListener {
         function(task)
     }
     binding.checkBox.setOnClickListener {
         update(task)
     }
 }
}
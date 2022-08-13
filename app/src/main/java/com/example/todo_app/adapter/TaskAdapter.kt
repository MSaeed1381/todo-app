package com.example.todo_app.adapter

import android.R.attr.data
import android.R.attr.switchTextAppearance
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.R
import com.example.todo_app.data.entities.Task
import com.example.todo_app.databinding.RowItemTaskBinding
import com.example.todo_app.recyclerFeatures.ItemMoveCallback
import java.util.*


class TaskAdapter(
    var tasks: List<Task>,
    private val changeSituation: (Task) -> Unit,
    private val update: (Task) -> Unit,
    val updateFunc: (Task, Boolean) -> Unit
): RecyclerView.Adapter<TaskViewHolder>(), ItemMoveCallback.ItemTouchHelperContract {

    var isNight: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:RowItemTaskBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.row_item_task, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(isNight, tasks[position], changeSituation, update)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }



    @SuppressLint("NotifyDataSetChanged")
    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                val temp = tasks[i].position
                tasks[i].position = tasks[i+1].position
                tasks[i+1].position = temp
                updateFunc(tasks[i], false)
                updateFunc(tasks[i+1], false)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tasks, i, i - 1);
                val temp = tasks[i].position
                tasks[i].position = tasks[i-1].position
                tasks[i-1].position = temp
                updateFunc(tasks[i], false)
                updateFunc(tasks[i-1], false)
            }
        }
        notifyDataSetChanged()
    }

    override fun onRowSelected(myViewHolder: TaskViewHolder?) {
        myViewHolder!!.setBackGround((0xFFE5E4E2).toInt());
    }

    override fun onRowClear(myViewHolder: TaskViewHolder?) {
        myViewHolder!!.setBackGround(Color.WHITE);
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
    fun setBackGround(color: Int){
        binding.itemCardView.setBackgroundColor(color)
    }

}
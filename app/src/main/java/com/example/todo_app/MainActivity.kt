package com.example.todo_app

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.data.Task
import com.example.todo_app.data.TaskDataBase
import com.example.todo_app.data.TaskRepository
import com.example.todo_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: TaskViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = TaskDataBase.getInstance(application).taskDao
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)

        binding.checkBox2.setOnCheckedChangeListener { _, _ ->
            if (binding.checkBox2.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.imageView4.setImageResource(R.drawable.bg_mobile_dark)
                binding.recyclerView.adapter = TaskAdapter(binding.checkBox2.isChecked,viewModel.tasks.value!!, { item: Task -> rowItemRemoveClick(item) }) { item: Task ->
                    checkBoxClicked(item)
                }
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.imageView4.setImageResource(R.drawable.bg_mobile_light)
            }
        }


        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
        binding.taskViewModel = viewModel
        binding.lifecycleOwner = this

        initRecyclerView(viewModel.tasks)

        viewModel.inputText.observe(this, Observer {
            if (it.isNotBlank()){
                binding.ibAdd.visibility = View.VISIBLE
            }else{
                binding.ibAdd.visibility = View.INVISIBLE
            }
        })


        viewModel.inProgressTasks.observe(this, Observer {
            binding.textView4.text = "${it.size} items left"
        })



        binding.tvClearCompleted.setOnClickListener {
            viewModel.deleteCompletedTask()
        }

        val moveOn: RecyclerViewMoveOn = object : RecyclerViewMoveOn(this){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                val temp = viewModel.tasks.value?.get(from)!!.position
                viewModel.tasks.value?.get(from)!!.position = viewModel.tasks.value?.get(to)!!.position
                viewModel.tasks.value?.get(to)!!.position = temp

                viewModel.update(viewModel.tasks.value?.get(from)!!, false)
                viewModel.update(viewModel.tasks.value?.get(to)!!, false)
                return false
            }

        }
        ItemTouchHelper(moveOn).attachToRecyclerView(binding.recyclerView)

    }
    private fun initRecyclerView(tasks: LiveData<List<Task>>){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        loadTasks(tasks)
    }

    private fun loadTasks(tasks: LiveData<List<Task>>){
        tasks.observe(this, Observer {
            binding.recyclerView.adapter = TaskAdapter(binding.checkBox2.isChecked,it, { item: Task -> rowItemRemoveClick(item) }) { item: Task ->
                checkBoxClicked(item)
            }
        })
    }


    private fun rowItemRemoveClick(task: Task){
        viewModel.deleteTask(task)
    }
    private fun checkBoxClicked(task: Task){
        viewModel.update(task, true)
    }
}
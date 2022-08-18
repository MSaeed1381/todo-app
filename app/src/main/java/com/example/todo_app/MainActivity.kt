package com.example.todo_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_app.adapter.TaskAdapter
import com.example.todo_app.data.TaskRepository
import com.example.todo_app.data.db.TaskDataBase
import com.example.todo_app.data.entities.Task
import com.example.todo_app.databinding.ActivityMainBinding
import com.example.todo_app.recyclerFeatures.ItemMoveCallback
import com.example.todo_app.viewmodel.TaskViewModel
import com.example.todo_app.viewmodel.factory.TaskViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter
    private var currentState = "all"

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = TaskDataBase.getInstance(application).taskDao
        val repository = TaskRepository.getInstance(dao)
        val factory = TaskViewModelFactory(repository)
        // night and dark mode implementation

        binding.nightLightCheckbox.setOnCheckedChangeListener { _, _ ->
            adapter.setLightMode(binding.nightLightCheckbox.isChecked)
            if (binding.nightLightCheckbox.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.imageView4.setImageResource(R.drawable.bg_mobile_dark)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.imageView4.setImageResource(R.drawable.bg_mobile_light)
            }
        }

        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]
        binding.taskViewModel = viewModel
        binding.lifecycleOwner = this

        initRecyclerView()

        viewModel.getTasks().observe(this) {
            updateList()
            binding.textView4.text = "${viewModel.getActiveTasks().size} items left"
        }

        // change state (all, active, completed)
        binding.bottomNavigationView.setOnItemSelectedListener {
            currentState = when (it.itemId) {
                R.id.activeTasks -> "active"
                R.id.completedTasks -> "completed"
                else -> "all"
            }
            it.isChecked = true
            updateList()
            false
        }


        // check edit text to add task
        viewModel.inputText.observe(this) {
            binding.ibAdd.visibility = if (it.isNotBlank()) View.VISIBLE else View.INVISIBLE
        }
        binding.tvClearCompleted.setOnClickListener {
            viewModel.deleteCompletedTask()
        }
    }

    private fun initRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        loadTasks()
    }

    private fun loadTasks(){
        try {
            adapter = TaskAdapter(
                { item: Task -> rowItemRemoveClick(item) },
                { item: Task -> checkBoxClicked(item)    })
                { item: Task, b:Boolean -> viewModel.update(item, b) }

            val callback: ItemTouchHelper.Callback = ItemMoveCallback(adapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.recyclerView)
            binding.recyclerView.adapter = adapter
        }catch (e: Exception){
            println("exception occurred")
        }
    }

    private fun rowItemRemoveClick(task: Task){
        viewModel.deleteTask(task)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun checkBoxClicked(task: Task){
        viewModel.update(task, true)
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(){
        var state = viewModel.getArrayTasks()
        if (currentState == "active"){
            state = viewModel.getActiveTasks()
        }else if (currentState == "completed"){
            state = viewModel.getCompletedTasks()
        }
            adapter.submitList(state)
    }
}
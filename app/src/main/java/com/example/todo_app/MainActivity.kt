package com.example.todo_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
            binding.textView4.text = "${it.size.toString()} items left"
        })

        binding.tvClearCompleted.setOnClickListener {
            viewModel.deleteCompletedTask()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.allTasks -> print(viewModel.tasks.toString())
                R.id.completedTasks -> print(viewModel.completedTasks)
                R.id.activeTasks -> print(viewModel.inProgressTasks)
            }
            true
        }

        binding.ibAdd.setOnClickListener {
            initRecyclerView(viewModel.tasks)
        }

    }
    private fun initRecyclerView(tasks: LiveData<List<Task>>){
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        loadTasks(tasks)
    }
    private fun loadTasks(tasks: LiveData<List<Task>>){
        tasks.observe(this, Observer {
            binding.recyclerView.adapter = TaskAdapter(it, { item: Task -> rowItemRemoveClick(item) }) { item: Task ->
                checkBoxClicked(item)
            }
        })
    }


    private fun rowItemRemoveClick(task: Task){
        viewModel.deleteTask(task)
    }
    private fun checkBoxClicked(task: Task){
        viewModel.update(task)
    }
}
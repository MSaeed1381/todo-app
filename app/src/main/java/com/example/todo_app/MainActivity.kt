package com.example.todo_app

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
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
    var adapter: TaskAdapter? = null
    private var currentState: LiveData<List<Task>>? = null

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = TaskDataBase.getInstance(application).taskDao
        val repository = TaskRepository(dao)
        val factory = TaskViewModelFactory(repository)

        binding.checkBox2.setOnCheckedChangeListener { _, _ ->
            adapter!!.isNight = binding.checkBox2.isChecked
            binding.recyclerView.adapter = adapter
            updateList()
            if (binding.checkBox2.isChecked) {
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
        viewModel.position.observe(this, Observer {

        })


        viewModel.tasks.observe(this, Observer {
            updateList()
        })

        binding.bottomNavigationView.setOnItemSelectedListener {
            currentState = if (it.itemId == R.id.allTasks){
                viewModel.tasks
            }else if (it.itemId == R.id.activeTasks){
                viewModel.inProgressTasks
            }else if (it.itemId == R.id.completedTasks){
                viewModel.completedTasks
            }else{
                viewModel.tasks
            }
            adapter!!.tasks = currentState!!.value!!
            binding.recyclerView.adapter = adapter
            adapter!!.notifyDataSetChanged()
            false
        }



        viewModel.inputText.observe(this, Observer {
            if (it.isNotBlank()){
                binding.ibAdd.visibility = View.VISIBLE
            }else{
                binding.ibAdd.visibility = View.INVISIBLE
            }
        })


        viewModel.inProgressTasks.observe(this, Observer {
            updateList()
            binding.textView4.text = "${it.size} items left"
        })
        viewModel.completedTasks.observe(this, Observer {
            updateList()
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
        adapter = TaskAdapter(binding.checkBox2.isChecked,tasks.value!!, { item: Task -> rowItemRemoveClick(item) }) { item: Task ->
            checkBoxClicked(item)
        }

        binding.recyclerView.adapter = adapter
    }



    private fun rowItemRemoveClick(task: Task){
        viewModel.deleteTask(task)
    }


    private fun checkBoxClicked(task: Task){
        viewModel.update(task, true)
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(){
        if (adapter == null){
            currentState = viewModel.tasks
            initRecyclerView(currentState!!)

        }else{
            adapter!!.tasks = currentState!!.value!!
            binding.recyclerView.adapter = adapter
            adapter!!.notifyDataSetChanged()
        }
    }
}
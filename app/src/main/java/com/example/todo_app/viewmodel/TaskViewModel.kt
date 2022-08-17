package com.example.todo_app.viewmodel


import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.TaskRepository
import com.example.todo_app.data.entities.Task
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository): ViewModel(), Observable {
    private val tasks = repository.getAllTasks()

    @Bindable
    var inputText = MutableLiveData<String>()
    @Bindable
    val inputSituation = MutableLiveData<Boolean>()
    
    init {
        inputText.value = ""
        inputSituation.value = false
    }
    fun addTask(){
        if (inputText.value!!.isNotBlank()){
            insert(Task(0, inputText.value!!, inputSituation.value!!, findMaxPosition()+1))
            inputText.value = ""
            inputSituation.value = false
        }
    }
    fun deleteTask(task: Task){
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    private fun insert(task: Task){
        viewModelScope.launch {
            repository.insert(task)
        }
    }

    fun update(task: Task, sitChange: Boolean){
        if (sitChange){
            task.situation = !task.situation // toggle
        }
        viewModelScope.launch {
            println(task.situation)
            repository.update(task)
        }
    }

    fun deleteCompletedTask(){
        viewModelScope.launch {
            repository.deleteCompletedTask()
        }
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    fun getCompletedTasks(): ArrayList<Task>{
        val completedTasks = ArrayList<Task>()
        for (task in tasks.value!!){
            if (task.situation){
                completedTasks.add(task)
            }
        }
        return completedTasks
    }

    fun getActiveTasks(): ArrayList<Task>{
        val activeTasks = ArrayList<Task>()
        for (task in tasks.value!!){
            if (!task.situation){
                activeTasks.add(task)
            }
        }
        return activeTasks
    }

    private fun findMaxPosition(): Int{
      var max = 0
        for (task in tasks.value!!){
            if (task.position > max){
                max = task.position
            }
        }
        return max
    }

    fun getTasks() :LiveData<List<Task>>{
        return this.tasks
    }
    fun getArrayTasks(): ArrayList<Task>{
        val arrayTasks = ArrayList<Task>()
        if (tasks.value != null){
            for (task in tasks.value!!){
                arrayTasks.add(task)
            }
        }

        return arrayTasks
    }

}
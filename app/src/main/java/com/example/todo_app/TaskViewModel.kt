package com.example.todo_app


import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.Task
import com.example.todo_app.data.TaskRepository
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository): ViewModel(), Observable {
    val tasks = repository.tasks
    val completedTasks = repository.completedTasks
    val inProgressTasks = repository.inProgressTasks

    lateinit var task: Task
    @Bindable
    var inputText = MutableLiveData<String>()
    @Bindable
    val inputSituation = MutableLiveData<Boolean>()
    companion object{
        var position = 1
    }

    init {
        inputText.value = ""
        inputSituation.value = false
    }
    fun addTask(){
        if (inputText.value!!.isNotBlank()){
            insert(Task(0, inputText.value!!, inputSituation.value!!, position++))
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
    fun deleteAll(){
        viewModelScope.launch {
            repository.deleteAll()
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

}
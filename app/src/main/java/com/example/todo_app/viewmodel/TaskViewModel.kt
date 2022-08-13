package com.example.todo_app.viewmodel


import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.TaskRepository
import com.example.todo_app.data.entities.Task
import kotlinx.coroutines.launch


class TaskViewModel(private val repository: TaskRepository): ViewModel(), Observable {
    val tasks = repository.tasks
    val completedTasks = repository.completedTasks
    val inProgressTasks = repository.inProgressTasks
    var position = repository.maxPos

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
            var p = 1
            if (position.value != null){
                p = position.value!! + 1
            }
            insert(Task(0, inputText.value!!, inputSituation.value!!, p))

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

}
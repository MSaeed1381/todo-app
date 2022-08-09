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

    @Bindable
    var inputText = MutableLiveData<String>()
    @Bindable
    val inputSituation = MutableLiveData<Boolean>()

    init {
        inputText.value = ""
        inputSituation.value = false
    }

    fun addTask(){
        println(inputSituation.value!!)
        insert(Task(0, inputText.value!!, inputSituation.value!!))
        inputText.value = ""
        inputSituation.value = false
    }
    fun deleteTask(){

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

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}
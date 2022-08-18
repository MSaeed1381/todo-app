package com.example.todo_app.data

import androidx.lifecycle.LiveData
import com.example.todo_app.data.dao.TaskDao
import com.example.todo_app.data.entities.Task

// singleton class

class TaskRepository private constructor(private val dao: TaskDao) {

    companion object {
        private var instance: TaskRepository? = null

        fun getInstance(dao: TaskDao): TaskRepository {
            synchronized(this) { // thread safe
                if (instance == null) {
                    instance = TaskRepository(dao = dao)
                }
            }
        return instance!!
        }
    }


     fun getAllTasks(): LiveData<List<Task>> {
        return dao.getAllTasks()
    }
    suspend fun insert(task: Task){
        dao.insertTask(task)
    }

    suspend fun delete(task: Task){
        dao.deleteTask(task)
    }


    suspend fun update(task: Task){
        dao.updateTask(task)
    }
    suspend fun deleteCompletedTask(){
        dao.deleteCompletedTasks()
    }

}
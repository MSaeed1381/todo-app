package com.example.todo_app.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.example.todo_app.data.db.TaskDataBase
import com.example.todo_app.data.entities.Task

// singleton class

class TaskRepository private constructor(private val dataBase: TaskDataBase) {

    companion object {
        private var instance: TaskRepository? = null

        fun getInstance(context: Context): TaskRepository {
            synchronized(this) { // thread safe
                if (instance == null) {
                    instance = TaskRepository(TaskDataBase.getInstance(context))
                }
            }
        return instance!!
        }
    }


     fun getAllTasks(): LiveData<List<Task>> {
        return dataBase.taskDao.getAllTasks()
    }
    suspend fun insert(task: Task){
        dataBase.taskDao.insertTask(task)
    }

    suspend fun delete(task: Task){
        dataBase.taskDao.deleteTask(task)
    }


    suspend fun update(task: Task){
        dataBase.taskDao.updateTask(task)
    }
    suspend fun deleteCompletedTask(){
        dataBase.taskDao.deleteCompletedTasks()
    }

    suspend fun updateIndexes(tasks: ArrayList<Task>){
        dataBase.withTransaction {
            for (i in 0 until tasks.size) {
                dataBase.taskDao.updatePositions(tasks[i].id, i+1)
            }
        }
    }

}
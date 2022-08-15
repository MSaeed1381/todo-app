package com.example.todo_app.data

import com.example.todo_app.data.dao.TaskDao
import com.example.todo_app.data.entities.Task

class TaskRepository(private val dao: TaskDao) {
    var tasks = dao.getAllTasks() //TODO
    var completedTasks = dao.getCompletedTasks()
    var inProgressTasks = dao.getInProgressTasks()
    var maxPos = dao.getMaxPosition()

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
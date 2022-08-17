package com.example.todo_app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo_app.data.entities.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)
    @Delete
    suspend fun deleteTask(task: Task)
    @Update
    suspend fun updateTask(task: Task)


    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table ORDER BY task_position")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("DELETE FROM task_table WHERE task_situation == 1")
    suspend fun deleteCompletedTasks()

}
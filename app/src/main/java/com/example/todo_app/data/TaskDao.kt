package com.example.todo_app.data

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Query("SELECT * FROM task_table WHERE task_situation == 1")
    fun getCompletedTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE task_situation == 0")
    fun getInProgressTasks(): LiveData<List<Task>>

    @Query("DELETE FROM task_table WHERE task_situation == 1")
    suspend fun deleteCompletedTasks()

}
package com.example.todo_app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todo_app.data.dao.TaskDao
import com.example.todo_app.data.entities.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDataBase: RoomDatabase() {
    abstract val taskDao: TaskDao
    companion object{
        private var INSTANCE: TaskDataBase? = null
        fun getInstance(context: Context): TaskDataBase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                    TaskDataBase::class.java,
                    "task_database").build()
                }
                return instance
            }
        }
    }
}
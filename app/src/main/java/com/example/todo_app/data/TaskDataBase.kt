package com.example.todo_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskDataBase: RoomDatabase() {
    abstract val taskDao: TaskDao
    companion object{
        private var INSTANCE: TaskDataBase? = null
        fun getInstance(context: Context): TaskDataBase{
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
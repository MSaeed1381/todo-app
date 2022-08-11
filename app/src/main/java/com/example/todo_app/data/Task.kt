package com.example.todo_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.todo_app.Situation


@Entity (tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id: Int,

    @ColumnInfo(name = "task_text")
    val text: String,

    @ColumnInfo(name = "task_situation")
    var situation: Boolean,

    @ColumnInfo(name = "task_position")
    var position: Int,

)

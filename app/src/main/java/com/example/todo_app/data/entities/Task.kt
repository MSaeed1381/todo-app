package com.example.todo_app.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo_app.adapter.TaskViewHolder


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

) : Comparable<Any> {
    override fun compareTo(other: Any): Int {
        if (other is Task){
            return if (this.position > other.position) 1 else 0
        }
        return -1
    }
}

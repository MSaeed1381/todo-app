package com.example.todo_app.data

class TaskRepository(private val dao: TaskDao) {
    var tasks = dao.getAllTasks()
    var completedTasks = dao.getCompletedTasks()
    var inProgressTasks = dao.getInProgressTasks()

    suspend fun insert(task: Task){
        dao.insertTask(task)
    }

    suspend fun delete(task: Task){
        dao.deleteTask(task)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

    suspend fun update(task: Task){
        dao.updateTask(task)
    }
    suspend fun deleteCompletedTask(){
        dao.deleteCompletedTasks()
    }


}
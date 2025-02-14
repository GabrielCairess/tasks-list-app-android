package br.com.tasks.database

import kotlinx.coroutines.flow.Flow
import br.com.model.Task

interface TaskRepository {
    fun getAll(): Flow<List<Task>>
    suspend fun getById(id: Long): Task?
    suspend fun insert(title: String, description: String?)
    suspend fun delete(id: Long)
    suspend fun completeTask(id: Long, completed: Boolean)
}
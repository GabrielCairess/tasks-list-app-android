package br.com.tasks.database

import br.com.model.Task
import br.com.tasks.utils.toTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override fun getAll(): Flow<List<Task>> {
        return taskDao.getAll().map {
            it.map { taskEntity ->
                taskEntity.toTask()
            }
        }

    }

    override suspend fun getById(id: Long): Task? {
        val task = taskDao.getById(id)
        return task?.toTask()
    }

    override suspend fun insert(title: String, description: String?) {
        val taskEntity = Task(title = title, description = description)
        taskDao.insert(taskEntity)
    }

    override suspend fun delete(id: Long) {
        val task = taskDao.getById(id) ?: return
        taskDao.delete(task)
    }
}
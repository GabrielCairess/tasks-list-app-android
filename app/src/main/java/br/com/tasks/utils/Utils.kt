package br.com.tasks.utils

import br.com.model.Task
import br.com.tasks.database.Task as TaskEntity

fun Task.toTaskEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}

fun TaskEntity.toTask(): Task {
    return Task(
        id = id ?: 0,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}
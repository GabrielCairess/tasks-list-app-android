package br.com.model

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean
)

val task = Task(
    id = 1,
    title = "Primeira tarefa",
    description = "Description da primeira tarefa",
    isCompleted = false
)

val task2 = Task(
    id = 2,
    title = "Segunda Tarefa",
    description = "Description da Segunda tarefa",
    isCompleted = true
)


//testando aqui

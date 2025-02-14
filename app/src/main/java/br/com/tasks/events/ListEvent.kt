package br.com.tasks.events


sealed interface ListEvent {
    data class DeleteTask(val id: Long) : ListEvent
    data class CompleteTask(val id: Long, val completed: Boolean) : ListEvent
    data class EditTask(val id: Long) : ListEvent
}
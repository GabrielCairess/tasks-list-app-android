package br.com.tasks.events

sealed interface AddEditEvent {
    data class TitleChanged(var title: String) : AddEditEvent
    data class DescriptionChanged(var description: String) : AddEditEvent
    object Save : AddEditEvent
}
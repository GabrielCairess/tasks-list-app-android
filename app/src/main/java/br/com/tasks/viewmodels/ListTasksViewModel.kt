package br.com.tasks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tasks.database.TaskRepository
import br.com.tasks.events.ListEvent
import br.com.tasks.events.UiEvent
import br.com.tasks.navigation.AddEditTasksScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListTasksViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks = repository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiEvent: Channel<UiEvent> = Channel()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.DeleteTask -> {
                viewModelScope.launch {
                    repository.delete(event.id)
                }
            }

            is ListEvent.CompleteTask -> {
                viewModelScope.launch {
                    repository.completeTask(event.id, event.completed)
                }
            }

            is ListEvent.EditTask -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AddEditTasksScreen(event.id)))
                }

            }
        }
    }
}
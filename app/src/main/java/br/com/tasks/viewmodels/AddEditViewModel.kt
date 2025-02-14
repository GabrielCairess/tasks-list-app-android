package br.com.tasks.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.tasks.database.TaskRepository
import br.com.tasks.events.AddEditEvent
import br.com.tasks.events.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val taskId: Long? = null,
    private val repository: TaskRepository
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        if (taskId != null) {
            viewModelScope.launch {
                repository.getById(taskId)?.let {
                    title = it.title
                    description = it.description
                }
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.TitleChanged -> {
                title = event.title
            }

            is AddEditEvent.DescriptionChanged -> {
                description = event.description
            }

            is AddEditEvent.Save -> {
                saveTask()
            }
        }
    }

    private fun saveTask() {
        viewModelScope.launch {

            if (title.isBlank()) {
                _uiEvent.send(UiEvent.showSnackbar("Title cannot be empty"))
                return@launch
            }

            repository.insert(taskId, title, description)
            _uiEvent.send(UiEvent.NavigateBack)
        }
    }

}
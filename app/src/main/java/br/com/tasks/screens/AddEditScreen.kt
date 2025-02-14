package br.com.tasks.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.tasks.database.DatabaseProvider
import br.com.tasks.database.TaskRepositoryImpl
import br.com.tasks.events.AddEditEvent
import br.com.tasks.events.UiEvent
import br.com.tasks.viewmodels.AddEditViewModel
import kotlinx.coroutines.launch

@Composable
fun AddEditScreen(
    taskId: Long?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dao = DatabaseProvider.getDatabase(context.applicationContext).taskDao
    val repository = TaskRepositoryImpl(taskDao = dao)

    val viewModel = viewModel {
        AddEditViewModel(taskId, repository)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        scope.launch {
            viewModel.uiEvent.collect {
                when (it) {
                    is UiEvent.showSnackbar -> {
                        snackbarHostState.showSnackbar(it.message)
                    }

                    is UiEvent.NavigateBack -> {
                        navigateBack()
                    }

                    else -> {}
                }
            }
        }
    }

    AddEditContent(
        title = viewModel.title,
        description = viewModel.description,
        snackbarHostState,
        viewModel::onEvent
    )
}

@Composable
fun AddEditContent(
    title: String,
    description: String?,
    snackbarHostState: SnackbarHostState,
    onEvent: (AddEditEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(AddEditEvent.Save)
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .consumeWindowInsets(paddingValues)
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            OutlinedTextField(
                value = title,
                onValueChange = {
                    onEvent(AddEditEvent.TitleChanged(it))
                },
                placeholder = {
                    Text(text = "Title")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description ?: "",
                onValueChange = {
                    onEvent(AddEditEvent.DescriptionChanged(it))
                },
                placeholder = {
                    Text(text = "Description")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun AddEdistScreenPreview() {
    AddEditScreen(
        taskId = null,
        navigateBack = {}
    )
}
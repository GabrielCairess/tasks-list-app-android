package br.com.tasks.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.model.Task
import br.com.model.task
import br.com.tasks.database.DatabaseProvider
import br.com.tasks.database.TaskRepositoryImpl
import br.com.tasks.events.ListEvent
import br.com.tasks.viewmodels.ListTasksViewModel

@Composable
fun ListTasks(
    navigateToAddEdit: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext
    val database = DatabaseProvider.getDatabase(context)
    val repository = TaskRepositoryImpl(database.taskDao)

    val viewModel = viewModel<ListTasksViewModel> {
        ListTasksViewModel(repository = repository)
    }

    ListContent(
        tasks = viewModel.tasks.collectAsState().value,
        navigateToAddEdit = {
            navigateToAddEdit(null)
        },
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ListContent(
    tasks: List<Task>,
    navigateToAddEdit: (Long?) -> Unit,
    onEvent: (ListEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigateToAddEdit(null)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(paddingValues),
            contentPadding = PaddingValues(30.dp)
        ) {
            itemsIndexed(tasks) { index, item ->
                TaskItem(
                    task = item,
                    onCompleteChanged = {
                        onEvent(ListEvent.CompleteTask(item.id, it))
                    },
                    onDelete = {
                        onEvent(ListEvent.DeleteTask(item.id))
                    },
                    onItemClick = {
                        onEvent(ListEvent.EditTask(item.id))
                    }
                )

                if (index < tasks.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

    }
}

@Composable
fun TaskItem(
    task: Task,
    onCompleteChanged: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(80.dp),
        shadowElevation = 2.dp,
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(checked = task.isCompleted, onCheckedChange = onCompleteChanged)

            Column(
                modifier = Modifier
                    .weight(1f),
            ) {
                Text(text = task.title, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = task.description ?: "", style = MaterialTheme.typography.bodyMedium)
            }

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    ListTasks({})
}

@Composable
private fun TaskItemPreview() {
    TaskItem(task = task, onCompleteChanged = {}, onDelete = {}, onItemClick = {})
}
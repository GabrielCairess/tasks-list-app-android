package br.com.tasks.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.tasks.screens.AddEditScreen
import br.com.tasks.screens.ListTasks
import kotlinx.serialization.Serializable

@Serializable
object ListTasksScreen

@Serializable
data class AddEditTasksScreen(val taskId: Long?)

@Composable
fun NavigationTaskApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ListTasksScreen,
        modifier = modifier
    ) {
        composable<ListTasksScreen> {
            ListTasks(
                navigateToAddEdit = {
                    navController.navigate(AddEditTasksScreen(it))
                }
            )
        }

        composable<AddEditTasksScreen> { backStackEntry ->
            val addEditTask = backStackEntry.toRoute<AddEditTasksScreen>()
            AddEditScreen()
        }
    }
}
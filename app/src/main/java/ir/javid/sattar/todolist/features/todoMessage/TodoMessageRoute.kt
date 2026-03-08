package ir.javid.sattar.todolist.features.todoMessage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageEvent
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageIntent
import kotlinx.coroutines.flow.Flow

@Composable
fun TodoMessageRoute(
    navController: NavHostController,
    todoId: Int,
    viewModel: TodoMessageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = todoId) {
        if (todoId != -1) {
            viewModel.sendIntent(TodoMessageIntent.LoadTodo(todoId))
        }
    }

    HandleTodoMessageEvents(
        events = viewModel.events,
        onSaveSuccess = {
            navController.popBackStack()
        }
    )

    TodoMessageScreen(
        state = uiState,
        onIntent = viewModel::sendIntent,
        navController = navController
    )
}

@Composable
fun HandleTodoMessageEvents(
    events: Flow<TodoMessageEvent>,
    onSaveSuccess: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        events.collect { event ->
            when (event) {
                is TodoMessageEvent.ShowSaveSuccess -> onSaveSuccess()
                is TodoMessageEvent.ShowError -> {
                    // Handle error
                }
            }
        }
    }
}

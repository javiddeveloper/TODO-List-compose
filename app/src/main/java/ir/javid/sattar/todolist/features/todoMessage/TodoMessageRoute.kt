package ir.javid.sattar.todolist.features.todoMessage

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.platform.LocalContext
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageEvent
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageIntent
import kotlinx.coroutines.flow.Flow

@Composable
fun TodoMessageRoute(
    todoId: Int,
    onNavigateBack: () -> Unit,
    viewModel: TodoMessageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = todoId) {
        if (todoId != -1) {
            viewModel.sendIntent(TodoMessageIntent.LoadTodo(todoId))
        }
    }

    HandleTodoMessageEvents(
        events = viewModel.events,
        snackbarHostState = snackbarHostState,
        onSaveSuccess = onNavigateBack
    )

    TodoMessageScreen(
        state = uiState,
        onIntent = viewModel::sendIntent,
        onBackClick = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun HandleTodoMessageEvents(
    events: Flow<TodoMessageEvent>,
    snackbarHostState: SnackbarHostState,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        events.collect { event ->
            when (event) {
                is TodoMessageEvent.ShowSaveSuccess -> onSaveSuccess()
                is TodoMessageEvent.ShowError -> snackbarHostState.showSnackbar(event.message.asString(context))
            }
        }
    }
}

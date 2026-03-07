package ir.javid.sattar.todolist.features.todoList.ui.todoMessage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract.TodoMessageEvent
import ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract.TodoMessageIntent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun TodoMessageRoute(
    navController: NavHostController,
    todoId: Int,
    viewModel: TodoMessageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    HandleTodoMessageEvents(
        events = viewModel.events,
        onNavigateBack = { navController.popBackStack() }
    )
    
    // Load todo if needed
    LaunchedEffect(key1 = todoId) {
        if (todoId != -1) {
            viewModel.sendIntent(TodoMessageIntent.LoadTodo(todoId))
        }
    }
    
    TodoMessageScreen(
        state = uiState,
        onIntent = viewModel::sendIntent,
        onSaveTodo = { todo ->
            viewModel.sendIntent(TodoMessageIntent.SaveTodo(todo))
        },
        onNavigateBack = { navController.popBackStack() }
    )
}

@Composable
fun HandleTodoMessageEvents(
    events: kotlinx.coroutines.flow.Flow<TodoMessageEvent>,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(key1 = true) {
        events.collect { event ->
            when (event) {
                is TodoMessageEvent.SaveSuccess -> {
                    scope.launch {
                        onNavigateBack()
                    }
                }
                is TodoMessageEvent.NavigateBack -> {
                    scope.launch {
                        onNavigateBack()
                    }
                }
                is TodoMessageEvent.SaveError -> {
                    // Handle error display (e.g., show snackbar)
                }
            }
        }
    }
}
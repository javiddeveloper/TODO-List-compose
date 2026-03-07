package ir.javid.sattar.todolist.features.todoList.ui.todoList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun TodoListRoute(
    navController: NavHostController,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagedTodos = viewModel.pagedTodos.collectAsLazyPagingItems()

    HandleTodoListEvents(
        events = viewModel.events,
        onNavigateToTodoMessage = { todoId ->
            navController.navigate("todo_message_screen/$todoId")
        }
    )

    TodoListScreen(
        state = uiState,
        pagedTodos = pagedTodos,
        onIntent = viewModel::sendIntent,
        navController = navController
    )
}

@Composable
fun HandleTodoListEvents(
    events: Flow<TodoListEvent>,
    onNavigateToTodoMessage: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        events.collect { event ->
            when (event) {
                is TodoListEvent.NavigateToTodoMessage -> {
                    scope.launch {
                        onNavigateToTodoMessage(event.todoId)
                    }
                }
                is TodoListEvent.ShowError -> {
                    // Handle error display (e.g., show snackbar)
                }
                is TodoListEvent.ShowDeleteSuccess -> {
                    // Handle delete success
                }
                is TodoListEvent.ShowPinSuccess -> {
                    // Handle pin success
                }
            }
        }
    }
}

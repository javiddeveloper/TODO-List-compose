package ir.javid.sattar.todolist.features.todoList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import ir.javid.sattar.todolist.R
import ir.javid.sattar.todolist.features.todoList.ui.todoList.TodoListScreen
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun TodoListRoute(
    onNavigateToTodoMessage: (Int) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagedTodos = viewModel.pagedTodos.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    HandleTodoListEvents(
        events = viewModel.events,
        snackbarHostState = snackbarHostState,
        onNavigateToTodoMessage = onNavigateToTodoMessage
    )

    TodoListScreen(
        state = uiState,
        pagedTodos = pagedTodos,
        onIntent = viewModel::sendIntent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun HandleTodoListEvents(
    events: Flow<TodoListEvent>,
    snackbarHostState: SnackbarHostState,
    onNavigateToTodoMessage: (Int) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        events.collect { event ->
            when (event) {
                is TodoListEvent.NavigateToTodoMessage -> onNavigateToTodoMessage(event.todoId)
                is TodoListEvent.ShowError -> snackbarHostState.showSnackbar(event.message.asString(context))
                is TodoListEvent.ShowDeleteSuccess -> snackbarHostState.showSnackbar(context.getString(R.string.delete_success))
                is TodoListEvent.ShowPinSuccess -> snackbarHostState.showSnackbar(context.getString(R.string.pin_success))
            }
        }
    }
}

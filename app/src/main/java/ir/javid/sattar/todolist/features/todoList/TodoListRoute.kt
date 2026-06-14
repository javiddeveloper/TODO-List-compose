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
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import ir.javid.sattar.todolist.features.todoList.ui.todoList.TodoListScreen
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

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
    val snackbarHostState = remember { SnackbarHostState() }

    HandleTodoListEvents(
        events = viewModel.events,
        snackbarHostState = snackbarHostState,
        onNavigateToTodoMessage = { todoId ->
            navController.navigate("todo_message_screen/$todoId")
        }
    )

    TodoListScreen(
        state = uiState,
        pagedTodos = pagedTodos,
        onIntent = viewModel::sendIntent,
        navController = navController,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun HandleTodoListEvents(
    events: Flow<TodoListEvent>,
    snackbarHostState: SnackbarHostState,
    onNavigateToTodoMessage: (Int) -> Unit
) {
    LaunchedEffect(key1 = true) {
        events.collect { event ->
            when (event) {
                is TodoListEvent.NavigateToTodoMessage -> onNavigateToTodoMessage(event.todoId)
                is TodoListEvent.ShowError -> snackbarHostState.showSnackbar(event.message)
                is TodoListEvent.ShowDeleteSuccess -> snackbarHostState.showSnackbar("آیتم با موفقیت حذف شد")
                is TodoListEvent.ShowPinSuccess -> snackbarHostState.showSnackbar("وضعیت پین بروزرسانی شد")
            }
        }
    }
}

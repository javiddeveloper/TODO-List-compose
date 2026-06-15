package ir.javid.sattar.todolist.feature.todo_list

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.EntryProviderScope
import androidx.compose.runtime.Composable
import ir.javid.sattar.todolist.core.ui.navigation.TodoNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
fun EntryProviderScope<NavKey>.todoListEntry(
    navigator: TodoNavigator
) {
    entry<TodoListRoots> {
        TodoListRoute(
            onNavigateToTodoMessage = { todoId ->
                navigator.navigateToTodoMessage(todoId)
            }
        )
    }
}

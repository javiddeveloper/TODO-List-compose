package ir.javid.sattar.todolist.feature.todo_detail

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.EntryProviderScope
import androidx.compose.runtime.Composable
import ir.javid.sattar.todolist.core.ui.navigation.TodoNavigator

fun EntryProviderScope<NavKey>.todoMessageEntry(
    navigator: TodoNavigator
) {
    entry<TodoMessageRoots> { key ->
        TodoMessageRoute(
            todoId = key.todoId,
            onNavigateBack = {
                navigator.navigateBack()
            }
        )
    }
}

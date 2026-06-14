package ir.javid.sattar.todolist.common.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import ir.javid.sattar.todolist.features.todoList.TodoListRoute
import ir.javid.sattar.todolist.features.todoMessage.TodoMessageRoute
import androidx.navigation3.ui.NavDisplay

@Composable
fun SetupNavGraph(backStack: NavBackStack<NavKey>) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeAt(backStack.lastIndex) },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Roots.TodoList> {
                TodoListRoute(
                    onNavigateToTodoMessage = { todoId ->
                        backStack.add(Roots.TodoMessage(todoId))
                    }
                )
            }
            entry<Roots.TodoMessage> { key ->
                TodoMessageRoute(
                    todoId = key.todoId,
                    onNavigateBack = {
                        backStack.removeAt(backStack.lastIndex)
                    }
                )
            }
        }
    )
}

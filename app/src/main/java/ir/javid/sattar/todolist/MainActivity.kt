package ir.javid.sattar.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import dagger.hilt.android.AndroidEntryPoint
import ir.javid.sattar.todolist.core.ui.navigation.TodoNavGraph
import ir.javid.sattar.todolist.core.ui.navigation.TodoNavigator
import ir.javid.sattar.todolist.core.ui.theme.TODOListTheme
import ir.javid.sattar.todolist.feature.todo_detail.TodoMessageRoots
import ir.javid.sattar.todolist.feature.todo_detail.todoMessageEntry
import ir.javid.sattar.todolist.feature.todo_list.TodoListRoots
import ir.javid.sattar.todolist.feature.todo_list.todoListEntry

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val backStack = rememberNavBackStack(TodoListRoots)
            val navigator = remember(backStack) {
                object : TodoNavigator {
                    override fun navigateToTodoList() {
                        backStack.add(TodoListRoots)
                    }

                    override fun navigateToTodoMessage(todoId: Int) {
                        backStack.add(TodoMessageRoots(todoId))
                    }

                    override fun navigateBack() {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.lastIndex)
                        } else {
                            finish()
                        }
                    }
                }
            }

            TODOListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.padding(it)) {
                        TodoNavGraph(
                            backStack = backStack,
                            navigator = navigator
                        ) { nav ->
                            todoListEntry(nav)
                            todoMessageEntry(nav)
                        }
                    }
                }
            }
        }
    }
}

package ir.javid.sattar.todolist.features.todoMessage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ir.javid.sattar.todolist.common.components.CustomTextField
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageIntent
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoMessageScreen(
    state: TodoMessageUiState,
    onIntent: (TodoMessageIntent) -> Unit,
    navController: NavHostController
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(state.todo) {
        state.todo?.let {
            title = it.title ?: ""
            message = it.message
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (state.todo == null) "Add Todo" else "Edit Todo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (state.todo == null) {
                            onIntent(TodoMessageIntent.SaveTodo(title, message))
                        } else {
                            onIntent(
                                TodoMessageIntent.UpdateTodo(
                                    state.todo.copy(title = title, message = message)
                                )
                            )
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                CustomTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Title",
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = "Message",
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    singleLine = false
                )
            }
        }
    )
}

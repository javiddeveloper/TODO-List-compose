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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.javid.sattar.todolist.R
import ir.javid.sattar.todolist.common.components.CustomTextField
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageIntent
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoMessageScreen(
    state: TodoMessageUiState,
    onIntent: (TodoMessageIntent) -> Unit,
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(state.todo) {
        state.todo?.let {
            title = it.title ?: ""
            message = it.message
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (state.todo == null) stringResource(R.string.add_todo) else stringResource(R.string.edit_todo)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (message.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(context.getString(R.string.message_empty_error))
                            }
                            return@IconButton
                        }
                        if (state.todo == null) {
                            onIntent(TodoMessageIntent.SaveTodo(title.ifBlank { null }, message))
                        } else {
                            onIntent(
                                TodoMessageIntent.UpdateTodo(
                                    state.todo.copy(
                                        title = title.ifBlank { null },
                                        message = message
                                    )
                                )
                            )
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = stringResource(R.string.save))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    placeholder = stringResource(R.string.title),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = stringResource(R.string.message),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    singleLine = false
                )
            }
        }
    )
}

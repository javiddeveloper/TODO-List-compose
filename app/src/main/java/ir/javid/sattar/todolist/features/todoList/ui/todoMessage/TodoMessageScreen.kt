package ir.javid.sattar.todolist.features.todoList.ui.todoMessage

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract.TodoMessageIntent
import ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract.TodoMessageUiState
import ir.javid.sattar.todolist.ui.components.CustomTextField
import ir.javid.sattar.todolist.ui.theme.TODOListTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun TodoMessageScreen(
    state: TodoMessageUiState,
    onIntent: (TodoMessageIntent) -> Unit,
    onSaveTodo: (TodoItem) -> Unit,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var isPinned by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    // Update local state when todo is loaded
    LaunchedEffect(state.currentTodo) {
        state.currentTodo?.let { todo ->
            title = todo.title ?: ""
            note = todo.message
            isPinned = todo.isPin
        }
    }

    // Handle errors
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                        onSaveTodo(
                            TodoItem(
                                id = state.currentTodo?.id ?: 0,
                                title = title,
                                message = note,
                                isPin = isPinned
                            )
                        )
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isPinned = !isPinned }) {
                        Icon(
                            if (isPinned) Icons.Default.Lock else Icons.Outlined.Lock,
                            contentDescription = "Pin/Unpin"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            CustomTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = "Title",
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = note,
                onValueChange = { note = it },
                placeholder = "Note",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
                focusRequester = focusRequester
            )
        }
    }
}

@Composable
fun TodoMessageScreenPreview() {
    TODOListTheme {
        val sampleState = TodoMessageUiState(
            currentTodo = TodoItem(
                id = 1,
                title = "تسک تستی",
                message = "این یک پیام تستی برای پیش‌نمایش است",
                isPin = true
            )
        )
        
        TodoMessageScreen(
            state = sampleState,
            onIntent = { /* No-op for preview */ },
            onSaveTodo = { /* No-op for preview */ },
            onNavigateBack = { /* No-op for preview */ }
        )
    }
}

@Composable
fun TodoMessageScreenEmptyPreview() {
    TODOListTheme {
        val emptyState = TodoMessageUiState()
        
        TodoMessageScreen(
            state = emptyState,
            onIntent = { /* No-op for preview */ },
            onSaveTodo = { /* No-op for preview */ },
            onNavigateBack = { /* No-op for preview */ }
        )
    }
}

@Composable
fun TodoMessageScreenLoadingPreview() {
    TODOListTheme {
        val loadingState = TodoMessageUiState(isLoading = true)
        
        TodoMessageScreen(
            state = loadingState,
            onIntent = { /* No-op for preview */ },
            onSaveTodo = { /* No-op for preview */ },
            onNavigateBack = { /* No-op for preview */ }
        )
    }
}
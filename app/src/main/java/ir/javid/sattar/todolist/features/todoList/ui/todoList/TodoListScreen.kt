package ir.javid.sattar.todolist.features.todoList.ui.todoList

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ir.javid.sattar.todolist.R
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListIntent
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListUiState
import ir.javid.sattar.todolist.ui.components.TodoTopBar
import ir.javid.sattar.todolist.ui.theme.TODOListTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalFoundationApi
@ExperimentalMaterial3Api
@ExperimentalCoroutinesApi
@Composable
fun TodoListScreen(
    state: TodoListUiState,
    onIntent: (TodoListIntent) -> Unit,
    navController: NavHostController,
) {
    val lazyListState = rememberLazyListState()
    
    Scaffold(
        topBar = {
            TodoTopBar(
                title = stringResource(id = R.string.app_name),
                selectedItem = state.selectedTodo,
                onAddClick = {
                    onIntent(TodoListIntent.AddNewTodo)
                },
                onDeleteClick = {
                    state.selectedTodo?.let { onIntent(TodoListIntent.DeleteTodo(it)) }
                },
                onCancelSelection = {
                    onIntent(TodoListIntent.NavigateToTodoMessage(-1)) // Clear selection
                },
                onPinClick = {
                    state.selectedTodo?.let {
                        onIntent(TodoListIntent.TogglePin(it.isPin.not(), it.id))
                    }
                })
        },
        content = { padding ->

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    if (state.todos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "You haven't any message\nPress + button to add new message",
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        ListContent(
                            todos = state.todos,
                            state = lazyListState,
                            selectedItem = state.selectedTodo,
                            itemClick = { item ->
                                onIntent(TodoListIntent.NavigateToTodoMessage(item.id))
                            },
                            itemLongClick = { item ->
                                // Handle long click for selection
                                // For now, we'll navigate directly
                                onIntent(TodoListIntent.NavigateToTodoMessage(item.id))
                            }
                        )
                    }
                }
            }
        }
    )
}

@ExperimentalFoundationApi
@Composable
fun ListContent(
    todos: List<TodoItem>,
    state: LazyListState,
    itemClick: (TodoItem) -> Unit,
    itemLongClick: (TodoItem) -> Unit,
    selectedItem: TodoItem?
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(vertical = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = todos.size,
            key = { index -> todos[index].id }
        ) { index ->
            val item = todos[index]
            TodoListItem(
                item = item,
                itemClick = itemClick,
                itemLongClick = itemLongClick,
                selectedItem = selectedItem
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun TodoListItem(
    item: TodoItem,
    itemClick: (TodoItem) -> Unit,
    itemLongClick: (TodoItem) -> Unit,
    selectedItem: TodoItem?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = {
                    if (selectedItem == null) {
                        itemClick(item)
                    } else {
                        itemLongClick(item)
                    }
                },
                onLongClick = { itemLongClick(item) }
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            width = if (selectedItem == item) 3.dp else 1.dp,
            color = if (selectedItem == item) Color.Blue else Color.Gray
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.isPin) {
                Box(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .width(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Pin/Unpin todo"
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (item.title != null)
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun TodoListScreenPreview() {
    TODOListTheme {
        val sampleState = TodoListUiState(
            todos = listOf(
                TodoItem(id = 1, title = "تسک اول", message = "این یک تسک تستی است", isPin = true),
                TodoItem(id = 2, title = "تسک دوم", message = "این هم یک تسک تستی دیگر", isPin = false),
                TodoItem(id = 3, title = null, message = "تسک بدون عنوان", isPin = false)
            )
        )
        
        TodoListScreen(
            state = sampleState,
            onIntent = { /* No-op for preview */ },
            navController = androidx.navigation.compose.rememberNavController()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun TodoListScreenLoadingPreview() {
    TODOListTheme {
        val loadingState = TodoListUiState(isLoading = true)
        
        TodoListScreen(
            state = loadingState,
            onIntent = { /* No-op for preview */ },
            navController = androidx.navigation.compose.rememberNavController()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalCoroutinesApi::class
)@Composable
fun TodoListScreenEmptyPreview() {
    TODOListTheme {
        val emptyState = TodoListUiState(todos = emptyList())
        
        TodoListScreen(
            state = emptyState,
            onIntent = { /* No-op for preview */ },
            navController = androidx.navigation.compose.rememberNavController()
        )
    }
}

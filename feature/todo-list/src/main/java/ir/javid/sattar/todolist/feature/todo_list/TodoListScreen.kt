package ir.javid.sattar.todolist.feature.todo_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ir.javid.sattar.todolist.core.domain.model.TodoItem
import ir.javid.sattar.todolist.core.ui.R
import ir.javid.sattar.todolist.feature.todo_list.contract.TodoListIntent
import ir.javid.sattar.todolist.feature.todo_list.contract.TodoListUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
@Composable
fun TodoListScreen(
    state: TodoListUiState,
    pagedTodos: LazyPagingItems<TodoItem>,
    onIntent: (TodoListIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            ModernBottomBar(
                onAddClick = { onIntent(TodoListIntent.AddNewTodo) },
                onCalendarClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Calendar feature coming soon!")
                    }
                },
                onSettingsClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Settings feature coming soon!")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                DayHeader(dayName = "Fri", date = "December 9 2024")
                WeekCalendar(selectedDayIndex = 4)
                
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    val isPagingLoading = pagedTodos.loadState.refresh is LoadState.Loading
                    
                    if (state.isLoading || isPagingLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        if (pagedTodos.itemCount == 0) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_messages),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 80.dp) // Extra padding for bottom bar
                            ) {
                                items(
                                    count = pagedTodos.itemCount,
                                    key = { index -> pagedTodos[index]?.id ?: index }
                                ) { index ->
                                    val item = pagedTodos[index]
                                    if (item != null) {
                                        SwipeToDismissItem(
                                            item = item,
                                            onDelete = { onIntent(TodoListIntent.DeleteTodo(item)) },
                                            onEdit = { onIntent(TodoListIntent.NavigateToTodoMessage(item.id)) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    item: TodoItem,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var show by remember { mutableStateOf(true) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    show = false
                    true
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit()
                    false
                }
                else -> false
            }
        }
    )

    LaunchedEffect(show) {
        if (!show) {
            kotlinx.coroutines.delay(300)
            onDelete()
        }
    }

    AnimatedVisibility(
        visible = show,
        exit = fadeOut(animationSpec = tween(durationMillis = 300)) + shrinkVertically(animationSpec = tween(durationMillis = 300))
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { DismissBackground(dismissState) },
            content = {
                val iconData = getIconForItem(item)
                TodoItemRow(
                    icon = iconData.first,
                    iconColor = iconData.second,
                    title = item.title ?: item.message,
                    time = if (item.title != null) "09:00" else null,
                    isCompleted = false,
                    modifier = Modifier
                        .background(Color.White)
                        .clickable { onEdit() }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFF9575CD) // Purple for Edit
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFE57373) // Red for Delete
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 24.dp),
        contentAlignment = when (dismissState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
            SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {
        Icon(
            imageVector = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Edit
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> Icons.Default.Delete
            },
            contentDescription = null,
            tint = Color.White
        )
    }
}

fun getIconForItem(item: TodoItem): Pair<ImageVector, Color> {
    return when {
        item.title?.contains("Birthday", ignoreCase = true) == true -> Icons.Default.Star to Color(0xFFE57373)
        item.title?.contains("Wake up", ignoreCase = true) == true -> Icons.Default.WbSunny to Color(0xFFFFB74D)
        item.title?.contains("Design", ignoreCase = true) == true -> Icons.Default.Brush to Color(0xFF9575CD)
        item.title?.contains("Haircut", ignoreCase = true) == true -> Icons.Default.ContentCut to Color(0xFF4FC3F7)
        item.title?.contains("Pasta", ignoreCase = true) == true -> Icons.Default.RadioButtonUnchecked to Color.Gray
        item.title?.contains("Wind down", ignoreCase = true) == true -> Icons.Default.NightsStay to Color(0xFF5C6BC0)
        else -> Icons.Default.RadioButtonUnchecked to Color.LightGray
    }
}

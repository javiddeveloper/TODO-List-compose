package ir.javid.sattar.todolist.feature.todo_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.javid.sattar.todolist.core.common.UiText
import ir.javid.sattar.todolist.core.common.ViewModelMVI
import ir.javid.sattar.todolist.core.domain.model.TodoItem
import ir.javid.sattar.todolist.core.domain.use_cases.DeleteTodoUseCase
import ir.javid.sattar.todolist.core.domain.use_cases.DeleteTodosUseCase
import ir.javid.sattar.todolist.core.domain.use_cases.PinTodoUseCase
import ir.javid.sattar.todolist.core.domain.use_cases.TodoListUseCase
import ir.javid.sattar.todolist.core.ui.R
import ir.javid.sattar.todolist.feature.todo_list.contract.TodoListEvent
import ir.javid.sattar.todolist.feature.todo_list.contract.TodoListIntent
import ir.javid.sattar.todolist.feature.todo_list.contract.TodoListUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TodoListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    todoListUseCase: TodoListUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val deleteTodosUseCase: DeleteTodosUseCase,
    private val pinTodoUseCase: PinTodoUseCase,
) : ViewModelMVI<TodoListUiState, TodoListUiState.PartialState, TodoListEvent, TodoListIntent>(
    savedStateHandle,
    TodoListUiState()
) {

    val pagedTodos: Flow<PagingData<TodoItem>> = todoListUseCase()
        .cachedIn(viewModelScope)

    override fun handleIntent(intent: TodoListIntent): Flow<TodoListUiState.PartialState> =
        when (intent) {
            is TodoListIntent.LoadTodos -> flow { emit(TodoListUiState.PartialState.Success()) }
            is TodoListIntent.DeleteTodo -> deleteTodo(intent.todo)
            is TodoListIntent.TogglePin -> togglePin(intent.isPin, intent.todoId)
            is TodoListIntent.ToggleSelect -> toggleSelect(intent.todoId)
            is TodoListIntent.ClearSelection -> clearSelection()
            is TodoListIntent.DeleteSelected -> deleteSelected()
            is TodoListIntent.NavigateToTodoMessage -> {
                sendEvent(TodoListEvent.NavigateToTodoMessage(intent.todoId))
                flow { }
            }
            is TodoListIntent.AddNewTodo -> {
                sendEvent(TodoListEvent.NavigateToTodoMessage(-1))
                flow { }
            }
        }

    private fun deleteTodo(todo: TodoItem): Flow<TodoListUiState.PartialState> = flow {
        try {
            deleteTodoUseCase(todo).collect()
            emit(TodoListUiState.PartialState.TodoDeleted(true))
            sendEvent(TodoListEvent.ShowDeleteSuccess)
        } catch (e: Exception) {
            val message = e.message?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_deleting_task)
            emit(TodoListUiState.PartialState.Error(message))
            sendEvent(TodoListEvent.ShowError(message))
        }
    }

    private fun togglePin(isPin: Boolean, todoId: Int): Flow<TodoListUiState.PartialState> = flow {
        try {
            pinTodoUseCase(todoId, isPin).collect()
            emit(TodoListUiState.PartialState.TodoPinned(true))
            sendEvent(TodoListEvent.ShowPinSuccess)
        } catch (e: Exception) {
            val message = e.message?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_pinning_task)
            emit(TodoListUiState.PartialState.Error(message))
            sendEvent(TodoListEvent.ShowError(message))
        }
    }

    private fun toggleSelect(todoId: Int): Flow<TodoListUiState.PartialState> = flow {
        val current = uiState.value.selectedIds.toMutableSet()
        if (current.contains(todoId)) {
            current.remove(todoId)
        } else {
            current.add(todoId)
        }
        emit(TodoListUiState.PartialState.SelectionChanged(current))
    }

    private fun clearSelection(): Flow<TodoListUiState.PartialState> = flow {
        emit(TodoListUiState.PartialState.SelectionChanged(emptySet()))
    }

    private fun deleteSelected(): Flow<TodoListUiState.PartialState> = flow {
        try {
            val ids = uiState.value.selectedIds.toList()
            if (ids.isNotEmpty()) {
                deleteTodosUseCase(ids).collect()
                emit(TodoListUiState.PartialState.TodoDeleted(true))
                sendEvent(TodoListEvent.ShowDeleteSuccess)
            } else {
                emit(TodoListUiState.PartialState.Success())
            }
        } catch (e: Exception) {
            val message = e.message?.let { UiText.DynamicString(it) }
                ?: UiText.StringResource(R.string.error_deleting_tasks)
            emit(TodoListUiState.PartialState.Error(message))
            sendEvent(TodoListEvent.ShowError(message))
        }
    }

    override fun reduceState(
        currentState: TodoListUiState,
        partialState: TodoListUiState.PartialState
    ): TodoListUiState {
        return when (partialState) {
            TodoListUiState.PartialState.Loading ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )

            is TodoListUiState.PartialState.Success ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = null
                )

            is TodoListUiState.PartialState.Error ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = partialState.message
                )

            is TodoListUiState.PartialState.TodoDeleted ->
                currentState.copy(
                    isLoading = false,
                    selectedTodo = null,
                    selectedIds = emptySet()
                )

            is TodoListUiState.PartialState.TodoPinned ->
                currentState.copy(
                    isLoading = false,
                    selectedTodo = null
                )

            is TodoListUiState.PartialState.TodoSelected ->
                currentState.copy(
                    selectedTodo = partialState.todo
                )
            is TodoListUiState.PartialState.SelectionChanged ->
                currentState.copy(
                    selectedIds = partialState.ids
                )
        }
    }

    override fun createErrorState(message: UiText): TodoListUiState.PartialState =
        TodoListUiState.PartialState.Error(message)
}

package ir.javid.sattar.todolist.features.todoList.ui.todoList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import ir.javid.sattar.todolist.features.todoList.domain.DeleteTodoUseCase
import ir.javid.sattar.todolist.features.todoList.domain.PinTodoUseCase
import ir.javid.sattar.todolist.features.todoList.domain.TodoListUseCase
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListEvent
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListIntent
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListUiState
import ir.javid.sattar.todolist.ui.mvi.ViewModelMVI
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
    private val pinTodoUseCase: PinTodoUseCase,
) : ViewModelMVI<TodoListUiState, TodoListUiState.PartialState, TodoListEvent, TodoListIntent>(
    savedStateHandle,
    TodoListUiState()
) {

    val pagedTodos: Flow<PagingData<TodoItem>> = todoListUseCase()
        .cachedIn(viewModelScope)

    init {
        // No need to manually trigger load with Paging 3 flow
    }

    override fun handleIntent(intent: TodoListIntent): Flow<TodoListUiState.PartialState> =
        when (intent) {
            is TodoListIntent.LoadTodos -> flow { emit(TodoListUiState.PartialState.Success()) } // No-op mostly
            is TodoListIntent.DeleteTodo -> deleteTodo(intent.todo)
            is TodoListIntent.TogglePin -> togglePin(intent.isPin, intent.todoId)
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
            emit(TodoListUiState.PartialState.Error(e.message ?: "خطا در حذف تسک"))
            sendEvent(TodoListEvent.ShowError(e.message ?: "خطا در حذف تسک"))
        }
    }

    private fun togglePin(isPin: Boolean, todoId: Int): Flow<TodoListUiState.PartialState> = flow {
        try {
            pinTodoUseCase(isPin, todoId).collect()
            emit(TodoListUiState.PartialState.TodoPinned(true))
            sendEvent(TodoListEvent.ShowPinSuccess)
        } catch (e: Exception) {
            emit(TodoListUiState.PartialState.Error(e.message ?: "خطا در پین کردن تسک"))
            sendEvent(TodoListEvent.ShowError(e.message ?: "خطا در پین کردن تسک"))
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
                    selectedTodo = null
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
        }
    }

    override fun createErrorState(message: String): TodoListUiState.PartialState =
        TodoListUiState.PartialState.Error(message)
}

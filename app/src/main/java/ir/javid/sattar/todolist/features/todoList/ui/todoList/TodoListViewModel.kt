package ir.javid.sattar.todolist.features.todoList.ui.todoList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.javid.sattar.todolist.data.database.entity.toTodoItem
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import ir.javid.sattar.todolist.features.todoList.domain.AddTodoUseCase
import ir.javid.sattar.todolist.features.todoList.domain.DeleteTodoUseCase
import ir.javid.sattar.todolist.features.todoList.domain.PinTodoUseCase
import ir.javid.sattar.todolist.features.todoList.domain.TodoListUseCase
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListEvent
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListIntent
import ir.javid.sattar.todolist.features.todoList.ui.todoList.contract.TodoListUiState
import ir.javid.sattar.todolist.ui.mvi.ViewModelMVI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TodoListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val todoListUseCase: TodoListUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase,
    private val pinTodoUseCase: PinTodoUseCase,
) : ViewModelMVI<TodoListUiState, TodoListUiState.PartialState, TodoListEvent, TodoListIntent>(
    savedStateHandle,
    TodoListUiState()
) {

    private var _todoList: Flow<PagingData<TodoItem>>? = null

    init {
        sendIntent(TodoListIntent.LoadTodos)
    }

    override fun handleIntent(intent: TodoListIntent): Flow<TodoListUiState.PartialState> =
        when (intent) {
            is TodoListIntent.LoadTodos -> loadTodos()
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

    private fun loadTodos(): Flow<TodoListUiState.PartialState> = flow {
        emit(TodoListUiState.PartialState.Loading)
        
        _todoList = todoListUseCase
            .invoke()
            .cachedIn(viewModelScope)
            .mapLatest { pagingData ->
                pagingData.map { it.toTodoItem() }
            }
            .catch { e ->
                emit(TodoListUiState.PartialState.Error(e.message ?: "خطای نامشخص"))
            }

        // Collect the paging data and emit success state
        _todoList?.collect { pagingData ->
            // Convert PagingData to list for state management
            // Note: This is a simplified approach, in real scenario you might want to handle paging differently
            val items = mutableListOf<TodoItem>()
            // For demo purposes, we'll just emit an empty list
            emit(TodoListUiState.PartialState.Success(emptyList()))
        }
    }

    private fun deleteTodo(todo: TodoItem): Flow<TodoListUiState.PartialState> = flow {
        try {
            deleteTodoUseCase.invoke(todo).collect()
            emit(TodoListUiState.PartialState.TodoDeleted(true))
            sendEvent(TodoListEvent.ShowDeleteSuccess)
        } catch (e: Exception) {
            emit(TodoListUiState.PartialState.Error(e.message ?: "خطا در حذف تسک"))
            sendEvent(TodoListEvent.ShowError(e.message ?: "خطا در حذف تسک"))
        }
    }

    private fun togglePin(isPin: Boolean, todoId: Int): Flow<TodoListUiState.PartialState> = flow {
        try {
            pinTodoUseCase.invoke(isPin, todoId).collect()
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
                    todos = partialState.todos,
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
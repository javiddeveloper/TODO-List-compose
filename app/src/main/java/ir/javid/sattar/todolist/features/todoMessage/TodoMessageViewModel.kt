package ir.javid.sattar.todolist.features.todoMessage

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.javid.sattar.todolist.domain.model.TodoItem
import ir.javid.sattar.todolist.domain.useCases.AddTodoUseCase
import ir.javid.sattar.todolist.domain.useCases.GetTodoUseCase
import ir.javid.sattar.todolist.domain.useCases.UpdateTodoUseCase
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageEvent
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageIntent
import ir.javid.sattar.todolist.features.todoMessage.contract.TodoMessageUiState
import ir.javid.sattar.todolist.common.mvi.ViewModelMVI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class TodoMessageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTodoUseCase: GetTodoUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase
) : ViewModelMVI<TodoMessageUiState, TodoMessageUiState.PartialState, TodoMessageEvent, TodoMessageIntent>(
    savedStateHandle,
    TodoMessageUiState()
) {

    override fun handleIntent(intent: TodoMessageIntent): Flow<TodoMessageUiState.PartialState> =
        when (intent) {
            is TodoMessageIntent.LoadTodo -> loadTodo(intent.todoId)
            is TodoMessageIntent.SaveTodo -> saveTodo(intent.title, intent.message)
            is TodoMessageIntent.UpdateTodo -> updateTodo(intent.todo)
        }

    private fun loadTodo(todoId: Int): Flow<TodoMessageUiState.PartialState> = flow {
        emit(TodoMessageUiState.PartialState.Loading)
        try {
            getTodoUseCase(todoId).collect { todo ->
                emit(TodoMessageUiState.PartialState.Success(todo))
            }
        } catch (e: Exception) {
            emit(TodoMessageUiState.PartialState.Error(e.message ?: "Error loading todo"))
        }
    }

    private fun saveTodo(title: String?, message: String): Flow<TodoMessageUiState.PartialState> = flow {
        emit(TodoMessageUiState.PartialState.Loading)
        try {
            addTodoUseCase(TodoItem(title = title, message = message, isPin = false)).collect()
            emit(TodoMessageUiState.PartialState.Saved)
            sendEvent(TodoMessageEvent.ShowSaveSuccess)
        } catch (e: Exception) {
            emit(TodoMessageUiState.PartialState.Error(e.message ?: "Error saving todo"))
            sendEvent(TodoMessageEvent.ShowError(e.message ?: "Error saving todo"))
        }
    }

    private fun updateTodo(todo: TodoItem): Flow<TodoMessageUiState.PartialState> = flow {
        emit(TodoMessageUiState.PartialState.Loading)
        try {
            updateTodoUseCase(todo).collect()
            emit(TodoMessageUiState.PartialState.Saved)
            sendEvent(TodoMessageEvent.ShowSaveSuccess)
        } catch (e: Exception) {
            emit(TodoMessageUiState.PartialState.Error(e.message ?: "Error updating todo"))
            sendEvent(TodoMessageEvent.ShowError(e.message ?: "Error updating todo"))
        }
    }

    override fun reduceState(
        currentState: TodoMessageUiState,
        partialState: TodoMessageUiState.PartialState
    ): TodoMessageUiState {
        return when (partialState) {
            TodoMessageUiState.PartialState.Loading -> currentState.copy(isLoading = true)
            is TodoMessageUiState.PartialState.Success -> currentState.copy(isLoading = false, todo = partialState.todo)
            is TodoMessageUiState.PartialState.Error -> currentState.copy(isLoading = false, errorMessage = partialState.message)
            TodoMessageUiState.PartialState.Saved -> currentState.copy(isLoading = false)
        }
    }

    override fun createErrorState(message: String): TodoMessageUiState.PartialState =
        TodoMessageUiState.PartialState.Error(message)
}

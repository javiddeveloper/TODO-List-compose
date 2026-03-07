package ir.javid.sattar.todolist.features.todoList.ui.todoMessage

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import ir.javid.sattar.todolist.features.todoList.domain.AddTodoUseCase
import ir.javid.sattar.todolist.features.todoList.domain.GetTodoUseCase
import ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract.TodoMessageEvent
import ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract.TodoMessageIntent
import ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract.TodoMessageUiState
import ir.javid.sattar.todolist.ui.mvi.ViewModelMVI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TodoMessageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addTodoUseCase: AddTodoUseCase,
    private val getTodoUseCase: GetTodoUseCase
) : ViewModelMVI<TodoMessageUiState, TodoMessageUiState.PartialState, TodoMessageEvent, TodoMessageIntent>(
    savedStateHandle,
    TodoMessageUiState()
) {

    override fun handleIntent(intent: TodoMessageIntent): Flow<TodoMessageUiState.PartialState> =
        when (intent) {
            is TodoMessageIntent.LoadTodo -> loadTodo(intent.todoId)
            is TodoMessageIntent.SaveTodo -> saveTodo(intent.todo)
            is TodoMessageIntent.NavigateBack -> {
                sendEvent(TodoMessageEvent.NavigateBack)
                emptyFlow()
            }
        }

    private fun loadTodo(todoId: Int): Flow<TodoMessageUiState.PartialState> = 
        getTodoUseCase.invoke(todoId)
            .map { todoItem ->
                TodoMessageUiState.PartialState.TodoLoaded(todoItem) as TodoMessageUiState.PartialState
            }
            .onStart { emit(TodoMessageUiState.PartialState.Loading) }
            .catch { e ->
                emit(TodoMessageUiState.PartialState.Error(e.message ?: "خطا در بارگذاری تسک"))
            }

    private fun saveTodo(todo: TodoItem): Flow<TodoMessageUiState.PartialState> = flow {
        emit(TodoMessageUiState.PartialState.Loading)
        
        if (todo.message.isEmpty()) {
            emit(TodoMessageUiState.PartialState.SaveSuccess(true))
            sendEvent(TodoMessageEvent.SaveSuccess)
            return@flow
        }
        
        addTodoUseCase.invoke(todo).collect {
            emit(TodoMessageUiState.PartialState.SaveSuccess(true))
            sendEvent(TodoMessageEvent.SaveSuccess)
        }
    }.catch { e ->
        emit(TodoMessageUiState.PartialState.Error(e.message ?: "خطا در ذخیره تسک"))
        sendEvent(TodoMessageEvent.SaveError(e.message ?: "خطا در ذخیره تسک"))
    }

    override fun reduceState(
        currentState: TodoMessageUiState,
        partialState: TodoMessageUiState.PartialState
    ): TodoMessageUiState {
        return when (partialState) {
            TodoMessageUiState.PartialState.Loading ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )

            is TodoMessageUiState.PartialState.TodoLoaded ->
                currentState.copy(
                    isLoading = false,
                    currentTodo = partialState.todo,
                    errorMessage = null
                )

            is TodoMessageUiState.PartialState.SaveSuccess ->
                currentState.copy(
                    isLoading = false,
                    isSaved = partialState.isSaved,
                    errorMessage = null
                )

            is TodoMessageUiState.PartialState.Error ->
                currentState.copy(
                    isLoading = false,
                    errorMessage = partialState.message
                )
        }
    }

    override fun createErrorState(message: String): TodoMessageUiState.PartialState {
        return TodoMessageUiState.PartialState.Error(message)
    }
}
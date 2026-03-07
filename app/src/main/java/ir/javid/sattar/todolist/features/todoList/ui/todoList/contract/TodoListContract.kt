package ir.javid.sattar.todolist.features.todoList.ui.todoList.contract

import androidx.compose.runtime.Immutable
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import kotlinx.parcelize.Parcelize

sealed class TodoListEvent {
    data class NavigateToTodoMessage(val todoId: Int) : TodoListEvent()
    data object ShowDeleteSuccess : TodoListEvent()
    data object ShowPinSuccess : TodoListEvent()
    data class ShowError(val message: String) : TodoListEvent()
}

sealed class TodoListIntent {
    data object LoadTodos : TodoListIntent()
    data class DeleteTodo(val todo: TodoItem) : TodoListIntent()
    data class TogglePin(val isPin: Boolean, val todoId: Int) : TodoListIntent()
    data class NavigateToTodoMessage(val todoId: Int) : TodoListIntent()
    data object AddNewTodo : TodoListIntent()
}

@Immutable
@Parcelize
data class TodoListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedTodo: TodoItem? = null
) : android.os.Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data class Success(val success: Boolean = true) : PartialState() // Changed from todos list
        data class Error(val message: String) : PartialState()
        data class TodoDeleted(val success: Boolean) : PartialState()
        data class TodoPinned(val success: Boolean) : PartialState()
        data class TodoSelected(val todo: TodoItem?) : PartialState()
    }
}
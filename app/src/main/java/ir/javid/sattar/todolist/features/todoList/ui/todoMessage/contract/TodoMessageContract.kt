package ir.javid.sattar.todolist.features.todoList.ui.todoMessage.contract

import androidx.compose.runtime.Immutable
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import kotlinx.parcelize.Parcelize

sealed class TodoMessageEvent {
    data object SaveSuccess : TodoMessageEvent()
    data class SaveError(val message: String) : TodoMessageEvent()
    data object NavigateBack : TodoMessageEvent()
}

sealed class TodoMessageIntent {
    data class LoadTodo(val todoId: Int) : TodoMessageIntent()
    data class SaveTodo(val todo: TodoItem) : TodoMessageIntent()
    data object NavigateBack : TodoMessageIntent()
}

@Immutable
@Parcelize
data class TodoMessageUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentTodo: TodoItem? = null,
    val isSaved: Boolean = false
) : android.os.Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data class TodoLoaded(val todo: TodoItem) : PartialState()
        data class SaveSuccess(val isSaved: Boolean) : PartialState()
        data class Error(val message: String) : PartialState()
    }
}
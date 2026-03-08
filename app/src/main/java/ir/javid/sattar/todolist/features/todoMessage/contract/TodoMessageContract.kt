package ir.javid.sattar.todolist.features.todoMessage.contract

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import ir.javid.sattar.todolist.domain.model.TodoItem
import kotlinx.parcelize.Parcelize

sealed class TodoMessageEvent {
    data object ShowSaveSuccess : TodoMessageEvent()
    data class ShowError(val message: String) : TodoMessageEvent()
}

sealed class TodoMessageIntent {
    data class LoadTodo(val todoId: Int) : TodoMessageIntent()
    data class SaveTodo(val title: String?, val message: String) : TodoMessageIntent()
    data class UpdateTodo(val todo: TodoItem) : TodoMessageIntent()
}

@Immutable
@Parcelize
data class TodoMessageUiState(
    val isLoading: Boolean = false,
    val todo: TodoItem? = null,
    val errorMessage: String? = null
) : Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data class Success(val todo: TodoItem) : PartialState()
        data class Error(val message: String) : PartialState()
        data object Saved : PartialState()
    }
}

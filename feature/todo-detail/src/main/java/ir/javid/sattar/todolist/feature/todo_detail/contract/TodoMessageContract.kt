package ir.javid.sattar.todolist.feature.todo_detail.contract

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import ir.javid.sattar.todolist.core.common.UiText
import ir.javid.sattar.todolist.core.domain.model.TodoItem
import kotlinx.parcelize.Parcelize

sealed class TodoMessageEvent {
    data object ShowSaveSuccess : TodoMessageEvent()
    data class ShowError(val message: UiText) : TodoMessageEvent()
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
    val errorMessage: UiText? = null
) : Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data class Success(val todo: TodoItem) : PartialState()
        data class Error(val message: UiText) : PartialState()
        data object Saved : PartialState()
    }
}

package ir.javid.sattar.todolist.common.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Roots : NavKey {
    @Serializable
    data object TodoList : Roots

    @Serializable
    data class TodoMessage(val todoId: Int = -1) : Roots
}

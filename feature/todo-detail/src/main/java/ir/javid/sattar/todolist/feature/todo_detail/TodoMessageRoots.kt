package ir.javid.sattar.todolist.feature.todo_detail

import ir.javid.sattar.todolist.core.ui.navigation.BaseRoots
import kotlinx.serialization.Serializable

@Serializable
data class TodoMessageRoots(val todoId: Int = -1) : BaseRoots

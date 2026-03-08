package ir.javid.sattar.todolist.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.javid.sattar.todolist.domain.model.TodoItem

@Entity
data class TodoItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val message: String,
    val isPin: Boolean = false,
)

fun TodoItemEntity.toTodoItem() = TodoItem(
    id = id,
    title = title,
    message = message,
    isPin = isPin,
)

fun TodoItem.toTodoItemEntity() = TodoItemEntity(
    id = id,
    title = title,
    message = message,
    isPin = isPin,
)

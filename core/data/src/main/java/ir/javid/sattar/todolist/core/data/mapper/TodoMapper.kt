package ir.javid.sattar.todolist.core.data.mapper

import ir.javid.sattar.todolist.core.domain.model.TodoItem
import ir.javid.sattar.todolist.core.local.model.TodoItemEntity

fun TodoItemEntity.toDomain() = TodoItem(
    id = id,
    title = title,
    message = message,
    isPin = isPin,
)

fun TodoItem.toEntity() = TodoItemEntity(
    id = id,
    title = title,
    message = message,
    isPin = isPin,
)

package ir.javid.sattar.todolist.features.todoList.domain

import androidx.paging.PagingData
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoListUseCase {
    operator fun invoke(): Flow<PagingData<TodoItem>>
}

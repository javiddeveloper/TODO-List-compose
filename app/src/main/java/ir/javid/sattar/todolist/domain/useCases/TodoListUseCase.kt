package ir.javid.sattar.todolist.domain.useCases

import androidx.paging.PagingData
import ir.javid.sattar.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoListUseCase {
    operator fun invoke(): Flow<PagingData<TodoItem>>
}

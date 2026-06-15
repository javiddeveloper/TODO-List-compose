package ir.javid.sattar.todolist.core.domain.use_cases

import androidx.paging.PagingData
import ir.javid.sattar.todolist.core.domain.model.TodoItem
import ir.javid.sattar.todolist.core.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoListUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(): Flow<PagingData<TodoItem>> =
        repository.getPagingTodos()
            .flowOn(Dispatchers.IO)
}
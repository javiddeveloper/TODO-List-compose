package ir.javid.sattar.todolist.domain.useCases

import androidx.paging.PagingData
import ir.javid.sattar.todolist.data.repository.TodoRepository
import ir.javid.sattar.todolist.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoListUseCaseImpl @Inject constructor(
    private val repository: TodoRepository
) : TodoListUseCase {
    override operator fun invoke(): Flow<PagingData<TodoItem>> =
        repository.getPagingTodos()
            .flowOn(Dispatchers.IO)
}

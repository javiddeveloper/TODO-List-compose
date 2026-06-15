package ir.javid.sattar.todolist.core.domain.use_cases

import ir.javid.sattar.todolist.core.domain.model.TodoItem
import ir.javid.sattar.todolist.core.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(todoId: Int): Flow<TodoItem> =
        repository.getTodo(todoId)
            .flowOn(Dispatchers.IO)
}
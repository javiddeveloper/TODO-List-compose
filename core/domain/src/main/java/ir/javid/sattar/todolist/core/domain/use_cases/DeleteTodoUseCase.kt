package ir.javid.sattar.todolist.core.domain.use_cases

import ir.javid.sattar.todolist.core.domain.repository.TodoRepository
import ir.javid.sattar.todolist.core.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(item: TodoItem): Flow<Int> =
        repository.deleteTodo(listOf(item.id))
            .flowOn(Dispatchers.IO)
}
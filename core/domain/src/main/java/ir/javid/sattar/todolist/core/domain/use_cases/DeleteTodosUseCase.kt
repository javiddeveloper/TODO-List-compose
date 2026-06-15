package ir.javid.sattar.todolist.core.domain.use_cases

import ir.javid.sattar.todolist.core.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteTodosUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(ids: List<Int>): Flow<Int> =
        repository.deleteTodo(ids)
            .flowOn(Dispatchers.IO)
}
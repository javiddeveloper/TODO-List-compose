package ir.javid.sattar.todolist.domain.useCases

import ir.javid.sattar.todolist.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteTodosUseCaseImpl @Inject constructor(
    private val repository: TodoRepository
) : DeleteTodosUseCase {
    override fun invoke(ids: List<Int>): Flow<Int> =
        repository.deleteTodo(ids)
            .flowOn(Dispatchers.IO)
}

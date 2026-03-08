package ir.javid.sattar.todolist.domain.useCases

import ir.javid.sattar.todolist.data.repository.TodoRepository
import ir.javid.sattar.todolist.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DeleteTodoUseCaseImpl @Inject constructor(
    private val repository: TodoRepository
) : DeleteTodoUseCase {
    override fun invoke(item: TodoItem): Flow<Int> =
        repository.deleteTodo(listOf(item.id))
            .flowOn(Dispatchers.IO)
}
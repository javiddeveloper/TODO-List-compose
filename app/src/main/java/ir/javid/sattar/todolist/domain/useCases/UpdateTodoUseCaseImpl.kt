package ir.javid.sattar.todolist.domain.useCases

import ir.javid.sattar.todolist.data.repository.TodoRepository
import ir.javid.sattar.todolist.domain.model.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateTodoUseCaseImpl @Inject constructor(
    private val repository: TodoRepository
) : UpdateTodoUseCase {
    override fun invoke(item: TodoItem): Flow<Unit> =
        repository.saveTodo(item)
            .flowOn(Dispatchers.IO)
}
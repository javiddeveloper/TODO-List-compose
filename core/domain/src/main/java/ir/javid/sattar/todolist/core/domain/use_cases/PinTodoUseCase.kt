package ir.javid.sattar.todolist.core.domain.use_cases

import ir.javid.sattar.todolist.core.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PinTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(todoId: Int, isPin: Boolean): Flow<Int> =
        repository.pinTodo(isPin, todoId)
            .flowOn(Dispatchers.IO)
}
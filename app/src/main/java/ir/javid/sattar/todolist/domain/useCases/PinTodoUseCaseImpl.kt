package ir.javid.sattar.todolist.domain.useCases

import ir.javid.sattar.todolist.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PinTodoUseCaseImpl @Inject constructor(
    private val repository: TodoRepository
) : PinTodoUseCase {
    override fun invoke(isPin: Boolean, todoId:Int): Flow<Int> =
        repository.pinTodo(isPin, todoId)
            .flowOn(Dispatchers.IO)
}
package ir.javid.sattar.todolist.domain.useCases

import kotlinx.coroutines.flow.Flow

interface PinTodoUseCase {
    operator fun invoke(isPin: Boolean, todoId:Int): Flow<Int>
}
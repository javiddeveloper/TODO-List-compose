package ir.javid.sattar.todolist.domain.useCases

import ir.javid.sattar.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface GetTodoUseCase {
    operator fun invoke(todoId: Int): Flow<TodoItem>
}
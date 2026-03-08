package ir.javid.sattar.todolist.domain.useCases

import ir.javid.sattar.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface DeleteTodoUseCase {
    operator fun invoke(item:TodoItem): Flow<Int>
}
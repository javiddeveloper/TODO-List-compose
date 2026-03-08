package ir.javid.sattar.todolist.domain.useCases

import kotlinx.coroutines.flow.Flow

interface DeleteTodosUseCase {
    operator fun invoke(ids: List<Int>): Flow<Int>
}

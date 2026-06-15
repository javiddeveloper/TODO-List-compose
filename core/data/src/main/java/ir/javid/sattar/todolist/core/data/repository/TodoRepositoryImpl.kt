package ir.javid.sattar.todolist.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import ir.javid.sattar.todolist.core.data.mapper.toDomain
import ir.javid.sattar.todolist.core.data.mapper.toEntity
import ir.javid.sattar.todolist.core.domain.model.TodoItem
import ir.javid.sattar.todolist.core.domain.repository.TodoRepository
import ir.javid.sattar.todolist.core.local.dao.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {

    override fun saveTodo(model: TodoItem): Flow<Unit> = flow {
        dao.upsertTodos(listOf(model.toEntity()))
        emit(Unit)
    }

    override fun getPagingTodos(): Flow<PagingData<TodoItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { dao.getAllTodos() }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getTodo(id: Int): Flow<TodoItem> = flow {
        val entity = dao.getTodo(id)
        emit(entity.toDomain())
    }

    override fun deleteTodo(ids: List<Int>): Flow<Int> = flow {
        val count = dao.deleteTodos(ids)
        emit(count)
    }

    override fun pinTodo(isPin: Boolean, todoId: Int): Flow<Int> = flow {
        val count = dao.pinTodo(isPin, todoId)
        emit(count)
    }
}

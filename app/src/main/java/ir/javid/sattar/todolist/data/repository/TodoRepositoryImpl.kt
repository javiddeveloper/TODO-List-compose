package ir.javid.sattar.todolist.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import ir.javid.sattar.todolist.data.database.dao.TodoDao
import ir.javid.sattar.todolist.data.database.entity.toTodoItem
import ir.javid.sattar.todolist.data.database.entity.toTodoItemEntity
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {

    override fun saveTodo(model: TodoItem): Flow<Unit> = flow {
        dao.upsertTodos(listOf(model.toTodoItemEntity()))
        emit(Unit)
    }

    override fun getPagingTodos(): Flow<PagingData<TodoItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { dao.getAllTodos() }
        ).flow.map { pagingData ->
            pagingData.map { it.toTodoItem() }
        }
    }

    override fun getTodo(id: Int): Flow<TodoItem> = flow {
        val entity = dao.getTodo(id)
        emit(entity.toTodoItem())
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

package ir.javid.sattar.todolist.core.domain.repository

import androidx.paging.PagingData
import ir.javid.sattar.todolist.core.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun saveTodo(model: TodoItem): Flow<Unit>

    fun getPagingTodos(): Flow<PagingData<TodoItem>>

    fun getTodo(id: Int): Flow<TodoItem>

    fun deleteTodo(ids: List<Int>): Flow<Int>

    fun pinTodo(isPin: Boolean, todoId:Int): Flow<Int>
}
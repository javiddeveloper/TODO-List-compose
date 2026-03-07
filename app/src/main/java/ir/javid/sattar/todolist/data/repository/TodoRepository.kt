package ir.javid.sattar.todolist.data.repository

import androidx.paging.PagingData
import ir.javid.sattar.todolist.data.database.entity.TodoItemEntity
import ir.javid.sattar.todolist.features.todoList.data.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun saveTodo(model: TodoItem): Flow<Unit>

    fun getPagingTodos(): Flow<PagingData<TodoItem>>

    fun getTodo(id: Int): Flow<TodoItem>

    fun deleteTodo(ids: List<Int>): Flow<Int>

    fun pinTodo(isPin: Boolean, todoId:Int): Flow<Int>
}
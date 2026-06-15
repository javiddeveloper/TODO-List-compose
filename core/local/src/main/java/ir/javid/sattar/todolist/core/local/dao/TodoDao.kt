package ir.javid.sattar.todolist.core.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.javid.sattar.todolist.core.local.model.TodoItemEntity

@Dao
interface TodoDao {
    @Query("SELECT * FROM TodoItemEntity order by isPin desc, id desc ")
    fun getAllTodos(): PagingSource<Int, TodoItemEntity>

    @Query("SELECT * FROM TodoItemEntity where id = :id")
    suspend fun getTodo(id: Int): TodoItemEntity

    @Upsert
    suspend fun upsertTodos(todos: List<TodoItemEntity>)

    @Query("DELETE FROM TodoItemEntity where id in (:ids)")
    suspend fun deleteTodos(ids: List<Int>): Int

    @Query("UPDATE TodoItemEntity set isPin = :isPin  where id in (:todoId)")
    suspend fun pinTodo(isPin: Boolean, todoId: Int): Int
}

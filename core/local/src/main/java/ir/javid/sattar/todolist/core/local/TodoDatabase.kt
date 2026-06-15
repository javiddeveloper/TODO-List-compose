package ir.javid.sattar.todolist.core.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.javid.sattar.todolist.core.local.dao.TodoDao
import ir.javid.sattar.todolist.core.local.model.TodoItemEntity

@Database(entities = [TodoItemEntity::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

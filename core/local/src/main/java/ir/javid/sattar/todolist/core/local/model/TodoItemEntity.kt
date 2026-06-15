package ir.javid.sattar.todolist.core.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String?,
    val message: String,
    val isPin: Boolean = false,
)

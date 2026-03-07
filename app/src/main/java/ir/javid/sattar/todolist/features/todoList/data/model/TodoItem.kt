package ir.javid.sattar.todolist.features.todoList.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    val id:Int = 0,
    val title:String?,
    val message:String,
    val isPin:Boolean,
) : Parcelable


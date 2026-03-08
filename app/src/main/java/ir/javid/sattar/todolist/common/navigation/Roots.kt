package ir.javid.sattar.todolist.common.navigation

sealed class Roots(val route: String){
    data object TodoList: Roots("todo_list_screen")
    data object TodoMessage: Roots("todo_message_screen")
}
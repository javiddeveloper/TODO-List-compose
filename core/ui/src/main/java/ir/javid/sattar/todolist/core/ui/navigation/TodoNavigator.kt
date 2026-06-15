package ir.javid.sattar.todolist.core.ui.navigation

interface TodoNavigator {
    fun navigateToTodoList()
    fun navigateToTodoMessage(todoId: Int = -1)
    fun navigateBack()
}

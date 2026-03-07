package ir.javid.sattar.todolist.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import ir.javid.sattar.todolist.domain.model.TodoItem
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopBar(
    title: String,
    selectedItem: TodoItem?,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCancelSelection: () -> Unit,
    onPinClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (selectedItem != null) {
                IconButton(onClick = onCancelSelection) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel selection"
                    )
                }
            }
        },
        actions = {
            if (selectedItem != null) {
                IconButton(onClick = onPinClick) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Pin/Unpin todo"
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete todo"
                    )
                }
            } else {
                IconButton(onClick = onAddClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add todo"
                    )
                }
            }
        }
    )
}
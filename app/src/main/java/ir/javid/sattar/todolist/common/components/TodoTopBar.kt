package ir.javid.sattar.todolist.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ir.javid.sattar.todolist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopBar(
    title: String,
    selectedCount: Int,
    selectionActive: Boolean,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCancelSelection: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = if (selectionActive) selectedCount.toString() else title) },
        navigationIcon = {
            if (selectionActive) {
                IconButton(onClick = onCancelSelection) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cancel_selection)
                    )
                }
            }
        },
        actions = {
            if (selectionActive) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_todo)
                    )
                }
            } else {
                IconButton(onClick = onAddClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_todo_desc)
                    )
                }
            }
        }
    )
}

package ir.javid.sattar.todolist.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.entryProvider

@Composable
fun TodoNavGraph(
    backStack: NavBackStack<NavKey>,
    navigator: TodoNavigator,
    featureEntries: EntryProviderScope<NavKey>.(TodoNavigator) -> Unit
) {
    NavDisplay(
        backStack = backStack,
        onBack = { navigator.navigateBack() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            featureEntries(navigator)
        }
    )
}

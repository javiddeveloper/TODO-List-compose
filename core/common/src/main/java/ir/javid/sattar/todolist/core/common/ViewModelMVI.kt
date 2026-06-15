package ir.javid.sattar.todolist.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import ir.javid.sattar.todolist.core.common.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

private const val SAVED_UI_STATE_KEY = "savedUiStateKey"

abstract class ViewModelMVI<STATE : android.os.Parcelable, PARTIAL_STATE, EVENT, INTENT>(
    savedStateHandle: SavedStateHandle,
    initialState: STATE
) : ViewModel() {

    private val intentChannel = Channel<INTENT>(Channel.UNLIMITED)

    private val _uiState = savedStateHandle.getStateFlow(SAVED_UI_STATE_KEY, initialState)
    val uiState: StateFlow<STATE> = _uiState

    private val eventChannel = Channel<EVENT>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            intentChannel.receiveAsFlow()
                .flatMapMerge { intent ->
                    handleIntent(intent)
                        .catch { error ->
                            val errorMessage = error.message?.let { UiText.DynamicString(it) }
                                ?: UiText.StringResource(R.string.unknown_error)
                            emit(createErrorState(errorMessage))
                        }
                }
                .scan(uiState.value) { currentState, partialState ->
                    reduceState(currentState, partialState)
                }
                .collect { newState ->
                    savedStateHandle[SAVED_UI_STATE_KEY] = newState
                }
        }
    }

    fun sendIntent(intent: INTENT) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    protected fun sendEvent(event: EVENT) : Flow<PARTIAL_STATE> {
        viewModelScope.launch {
            eventChannel.send(event)
        }
        return emptyFlow<PARTIAL_STATE>()
    }

    protected fun doAsyncTask(task: suspend () -> Unit): Flow<PARTIAL_STATE> {
        viewModelScope.launch {
            task.invoke()
        }
        return emptyFlow<PARTIAL_STATE>()
    }

    protected abstract fun handleIntent(intent: INTENT): Flow<PARTIAL_STATE>
    protected abstract fun reduceState(currentState: STATE, partialState: PARTIAL_STATE): STATE
    protected abstract fun createErrorState(message: UiText): PARTIAL_STATE
}
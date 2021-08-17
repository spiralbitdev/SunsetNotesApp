package spiral.bit.dev.sunsetnotesapp.ui.notes

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.domain.models.FilterPrefs
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.usecases.GetPreferenceFlowUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.UpdateSortOrderUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.DeleteNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.GetNotesUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.InsertNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.mappers.toFlowOfUiNotes
import spiral.bit.dev.sunsetnotesapp.mappers.toNote
import spiral.bit.dev.sunsetnotesapp.models.UINote
import spiral.bit.dev.sunsetnotesapp.util.ADD_RESULT_OK
import spiral.bit.dev.sunsetnotesapp.util.DELETE_RESULT_OK
import spiral.bit.dev.sunsetnotesapp.util.EDIT_RESULT_OK

@FlowPreview
@ExperimentalCoroutinesApi
class NotesViewModel(
    private val state: SavedStateHandle,
    private val insertNoteUseCaseImpl: InsertNoteUseCaseImpl,
    private val deleteNoteUseCaseImpl: DeleteNoteUseCaseImpl,
    private val getNotesUseCaseImpl: GetNotesUseCaseImpl,
    private val updateSortOrderUseCaseImpl: UpdateSortOrderUseCaseImpl,
    private val getPreferenceFlowUseCaseImpl: GetPreferenceFlowUseCaseImpl
) : ViewModel() {

    var searchQuery = state.get<String>("searchQuery") ?: ""
        set(value) {
            field = value
            state.set("searchQuery", value)
        }

    private val notesEventsChannel = Channel<NoteEvents>()
    val noteEvents = notesEventsChannel.receiveAsFlow()

    private val preferencesFlow: Flow<FilterPrefs> =
        getPreferenceFlowUseCaseImpl.getPreferenceFlow()

    val _searchQueryFlow = MutableStateFlow(searchQuery)
    private val searchQueryFlow: StateFlow<String> = _searchQueryFlow

    val notesFlow = preferencesFlow.zip(searchQueryFlow) { filterPrefs, query ->
        getNotesUseCaseImpl.get(query, filterPrefs.sortOrder).toFlowOfUiNotes()
    }.flattenConcat()

    fun onUndoDelete(uiNote: UINote) = viewModelScope.launch {
        uiNote.toNote().run {
            insertNoteUseCaseImpl.insert(this)
        }
    }

    fun onAddNewNoteClicked() = viewModelScope.launch {
        notesEventsChannel.send(NoteEvents.NavigateToAddNoteScreen)
    }

    fun onNoteSwiped(uiNote: UINote) = viewModelScope.launch {
        uiNote.toNote().run {
            deleteNoteUseCaseImpl.delete(this)
            notesEventsChannel.send(NoteEvents.ShowUndoDeleteSnackbar(uiNote))
        }
    }

    fun onAddEditResult(context: Context, result: Int) = viewModelScope.launch {
        with(context) {
            when (result) {
                ADD_RESULT_OK -> NoteEvents.ShowNoteSavedMsg(getString(R.string.note_successfully_saved))
                EDIT_RESULT_OK -> NoteEvents.ShowNoteSavedMsg(getString(R.string.note_edited))
                DELETE_RESULT_OK -> NoteEvents.ShowNoteSavedMsg(getString(R.string.note_deleted))
            }
        }
    }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        updateSortOrderUseCaseImpl.updateSortOrder(sortOrder)
    }

    fun onNoteSelected(uiNote: UINote) = viewModelScope.launch {
        notesEventsChannel.send(NoteEvents.NavigateToEditNoteScreen(uiNote))
    }

    sealed class NoteEvents {
        data class ShowNoteSavedMsg(val error: String) : NoteEvents()
        data class ShowUndoDeleteSnackbar(val note: UINote) : NoteEvents()
        data class NavigateToEditNoteScreen(val note: UINote) : NoteEvents()
        object NavigateToAddNoteScreen : NoteEvents()
    }
}
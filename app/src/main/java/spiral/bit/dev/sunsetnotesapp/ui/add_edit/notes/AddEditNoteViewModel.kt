package spiral.bit.dev.sunsetnotesapp.ui.add_edit.notes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.DeleteNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.InsertNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.UpdateNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.mappers.toNote
import spiral.bit.dev.sunsetnotesapp.models.UINote
import spiral.bit.dev.sunsetnotesapp.util.ADD_RESULT_OK
import spiral.bit.dev.sunsetnotesapp.util.UPDATE_RESULT_OK
import java.util.*

class AddEditNoteViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val insertNoteUseCaseImpl: InsertNoteUseCaseImpl,
    private val updateNoteUseCaseImpl: UpdateNoteUseCaseImpl
) : ViewModel() {

    private val noteEventsChannel = Channel<AddEditNoteEvents>()
    val noteEvents = noteEventsChannel.receiveAsFlow()

    val note = savedStateHandle.get<UINote>("note")

    var imageUri = savedStateHandle.get<String>("imageUri") ?: note?.imageUri ?: ""
        set(value) {
            field = value
            savedStateHandle.set("imageUri", value)
        }

    fun saveNoteClicked(title: String, subtitle: String, content: String) = viewModelScope.launch {
        if (!checkInvalidInput(title, subtitle)) return@launch

        note?.let {
            val updatedNote = note.copy(
                title = title,
                subTitle = subtitle,
                contentText = content,
                imageUri = imageUri
            )
            updateNote(updatedNote)
        } ?: run {
            val newNote = UINote(
                title = title,
                subTitle = subtitle,
                contentText = content,
                imageUri = imageUri
            )
            createNote(newNote)
        }
    }

    private fun updateNote(note: UINote) = viewModelScope.launch {
        note.toNote().run {
            updateNoteUseCaseImpl.update(this)
            noteEventsChannel.send(AddEditNoteEvents.NavigateBackWithResult(UPDATE_RESULT_OK))
        }
    }

    private fun createNote(note: UINote) = viewModelScope.launch {
        note.toNote().run {
            insertNoteUseCaseImpl.insert(this)
            noteEventsChannel.send(AddEditNoteEvents.NavigateBackWithResult(ADD_RESULT_OK))
        }
    }

    private suspend inline fun checkInvalidInput(title: String, subtitle: String) =
        viewModelScope.async {
            if (title.isNotEmpty() && subtitle.isNotEmpty()) {
                return@async true
            } else {
                noteEventsChannel.send(
                    AddEditNoteEvents.ShowInvalidInputMsg(R.string.fill_all_fields)
                )
                return@async false
            }
        }.await()
}

sealed class AddEditNoteEvents {
    data class ShowInvalidInputMsg(val msg: Int) : AddEditNoteEvents()
    data class NavigateBackWithResult(val result: Int) : AddEditNoteEvents()
}
package spiral.bit.dev.sunsetnotesapp.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import spiral.bit.dev.sunsetnotesapp.models.UINote
import spiral.bit.dev.sunsetnotesapp.domain.models.Note

fun Flow<List<Note>>.toFlowOfUiNotes(): Flow<List<UINote>> {
    val tempList = mutableListOf<UINote>()
    map { notesList ->
        notesList.forEach { note->
            tempList.add(note.toUINote())
        }
    }
    return flowOf(tempList)
}

fun Note.toUINote() = UINote(
    id, title, subTitle, contentText, createdDate, imageUri
)

fun UINote.toNote() = Note(
    id, title, subTitle, contentText, createdDate, imageUri
)
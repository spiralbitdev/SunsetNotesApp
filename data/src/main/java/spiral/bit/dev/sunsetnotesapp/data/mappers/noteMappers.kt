package spiral.bit.dev.sunsetnotesapp.data.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import spiral.bit.dev.sunsetnotesapp.data.models.NoteEntity
import spiral.bit.dev.sunsetnotesapp.domain.models.Note

fun Note.toNoteEntity() = NoteEntity(
    id,
    title,
    subTitle,
    contentText,
    createdDate,
    imageUri
)

fun NoteEntity.toNote() = Note(
    id,
    title,
    subTitle,
    contentText,
    createdDate,
    imageUri
)

fun Flow<List<NoteEntity>>.toFlowOfNotes(): Flow<List<Note>> {
    val tempList = mutableListOf<Note>()
    map { notesEntityList ->
        notesEntityList.forEach { noteEntity ->
            tempList.add(noteEntity.toNote())
        }
    }
    return flowOf(tempList)
}


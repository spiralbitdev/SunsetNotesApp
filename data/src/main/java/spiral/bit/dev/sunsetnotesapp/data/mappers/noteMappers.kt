package spiral.bit.dev.sunsetnotesapp.data.mappers

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

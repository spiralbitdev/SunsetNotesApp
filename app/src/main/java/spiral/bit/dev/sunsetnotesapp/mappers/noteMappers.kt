package spiral.bit.dev.sunsetnotesapp.mappers

import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.models.UINote

fun Note.toUINote() = UINote(
    id, title, subTitle, contentText, createdDate, imageUri
)

fun UINote.toNote() = Note(
    id, title, subTitle, contentText, createdDate, imageUri
)


package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces

import spiral.bit.dev.sunsetnotesapp.domain.models.Note

interface IInsertNoteUseCase {
    suspend fun insert(note: spiral.bit.dev.sunsetnotesapp.domain.models.Note)
}
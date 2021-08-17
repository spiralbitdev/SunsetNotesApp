package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces

import spiral.bit.dev.sunsetnotesapp.domain.models.Note

interface IDeleteNoteUseCase {
    suspend fun delete(note: spiral.bit.dev.sunsetnotesapp.domain.models.Note)
}
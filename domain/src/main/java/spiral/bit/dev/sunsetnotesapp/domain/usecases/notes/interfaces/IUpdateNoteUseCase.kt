package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces

import spiral.bit.dev.sunsetnotesapp.domain.models.Note

interface IUpdateNoteUseCase {
    suspend fun update(note: spiral.bit.dev.sunsetnotesapp.domain.models.Note)
}
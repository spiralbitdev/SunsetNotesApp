package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations

import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.repositories.INoteRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces.IInsertNoteUseCase

class InsertNoteUseCaseImpl(
    private val notesRepository: INoteRepository
) : IInsertNoteUseCase {
    override suspend fun insert(note: Note) {
        notesRepository.insert(note)
    }
}
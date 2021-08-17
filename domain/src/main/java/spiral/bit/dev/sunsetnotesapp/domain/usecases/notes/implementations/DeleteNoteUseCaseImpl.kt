package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations

import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.repositories.INoteRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces.IDeleteNoteUseCase

class DeleteNoteUseCaseImpl(
    private val notesRepository: INoteRepository
) : IDeleteNoteUseCase {
    override suspend fun delete(note: Note) {
        notesRepository.delete(note)
    }
}
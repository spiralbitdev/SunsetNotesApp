package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations

import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.repositories.INoteRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces.IUpdateNoteUseCase

class UpdateNoteUseCaseImpl(
    private val notesRepository: INoteRepository
) : IUpdateNoteUseCase {

    override suspend fun update(note: Note) {
        notesRepository.update(note)
    }
}
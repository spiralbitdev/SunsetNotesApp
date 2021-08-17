package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.repositories.INoteRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces.IGetNotesUseCase

class GetNotesUseCaseImpl(
    private val notesRepository: INoteRepository
) : IGetNotesUseCase {
    override fun get(searchQuery: String, sortOrder: SortOrder): Flow<List<Note>> {
        return notesRepository.get(searchQuery, sortOrder)
    }
}
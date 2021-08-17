package spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.interfaces

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder

interface IGetNotesUseCase {
    fun get(searchQuery: String, sortOrder: spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder): Flow<List<spiral.bit.dev.sunsetnotesapp.domain.models.Note>>
}
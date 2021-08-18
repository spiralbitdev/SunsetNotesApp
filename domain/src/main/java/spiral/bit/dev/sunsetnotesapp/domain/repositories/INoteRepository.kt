package spiral.bit.dev.sunsetnotesapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder

interface INoteRepository {

    suspend fun insert(note: Note)

    suspend fun update(note: Note)

    suspend fun delete(note: Note)

    fun get(searchQuery: String, sortOrder: SortOrder): Flow<List<Note>>
}
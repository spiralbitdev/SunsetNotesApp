package spiral.bit.dev.sunsetnotesapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder

interface INoteRepository {

    suspend fun insert(note: spiral.bit.dev.sunsetnotesapp.domain.models.Note)

    suspend fun update(note: spiral.bit.dev.sunsetnotesapp.domain.models.Note)

    suspend fun delete(note: spiral.bit.dev.sunsetnotesapp.domain.models.Note)

    fun get(searchQuery: String, sortOrder: spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder): Flow<List<spiral.bit.dev.sunsetnotesapp.domain.models.Note>>
}
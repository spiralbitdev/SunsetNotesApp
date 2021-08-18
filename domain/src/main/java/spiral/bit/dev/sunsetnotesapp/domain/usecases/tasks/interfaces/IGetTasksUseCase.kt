package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.models.Task

interface IGetTasksUseCase {
    suspend fun get(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>>
}
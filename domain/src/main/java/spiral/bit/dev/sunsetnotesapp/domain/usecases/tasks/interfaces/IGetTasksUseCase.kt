package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.models.Task

interface IGetTasksUseCase {
    fun get(searchQuery: String, sortOrder: spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder, hideCompleted: Boolean): Flow<List<spiral.bit.dev.sunsetnotesapp.domain.models.Task>>
}
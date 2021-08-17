package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.models.Task
import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces.IGetTasksUseCase

class GetTasksUseCaseImpl(
    private val taskRepository: ITaskRepository
) : IGetTasksUseCase {
    override fun get(
        searchQuery: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean
    ): Flow<List<Task>> {
        return taskRepository.get(searchQuery, sortOrder, hideCompleted)
    }
}
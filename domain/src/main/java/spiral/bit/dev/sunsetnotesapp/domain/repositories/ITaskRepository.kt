package spiral.bit.dev.sunsetnotesapp.domain.repositories

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.models.Task

interface ITaskRepository {

    suspend fun insert(task: Task)

    suspend fun update(task: Task)

    suspend fun delete(task: Task)

    suspend fun deleteAllCompletedTasks()

    suspend fun get(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>>
}
package spiral.bit.dev.sunsetnotesapp.data.repositories

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.data.db.TaskDao
import spiral.bit.dev.sunsetnotesapp.data.mappers.mapItems
import spiral.bit.dev.sunsetnotesapp.data.mappers.toTask
import spiral.bit.dev.sunsetnotesapp.data.mappers.toTaskEntity
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.models.Task
import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository

class TaskRepositoryImpl(private val taskDao: TaskDao) : ITaskRepository {

    override suspend fun insert(task: Task) {
        val mappedTask = task.toTaskEntity()
        taskDao.insert(mappedTask)
    }

    override suspend fun update(task: Task) {
        val mappedTask = task.toTaskEntity()
        taskDao.update(mappedTask)
    }

    override suspend fun delete(task: Task) {
        val mappedTask = task.toTaskEntity()
        taskDao.delete(mappedTask)
    }

    override suspend fun deleteAllCompletedTasks() {
        taskDao.deleteAllCompletedTasks()
    }

    override fun get(
        query: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean
    ): Flow<List<Task>> {
        return when (sortOrder) {
            SortOrder.BY_NAME -> taskDao.getTasksSortedByName(query, hideCompleted)
                .mapItems { it.toTask() }
            SortOrder.BY_DATE -> taskDao.getTasksSortedByDateCreated(
                query,
                hideCompleted
            ).mapItems { it.toTask() }
        }
    }
}
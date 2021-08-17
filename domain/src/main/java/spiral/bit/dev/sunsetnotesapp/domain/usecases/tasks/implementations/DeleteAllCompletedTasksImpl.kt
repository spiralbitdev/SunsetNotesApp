package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations

import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces.IDeleteAllCompletedTasks

class DeleteAllCompletedTasksImpl(
    private val taskRepository: ITaskRepository
) : IDeleteAllCompletedTasks {
    override suspend fun deleteAllCompletedTasks() {
        taskRepository.deleteAllCompletedTasks()
    }
}
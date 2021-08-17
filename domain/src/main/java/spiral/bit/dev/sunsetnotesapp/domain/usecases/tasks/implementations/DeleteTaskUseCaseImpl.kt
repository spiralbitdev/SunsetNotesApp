package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations

import spiral.bit.dev.sunsetnotesapp.domain.models.Task
import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces.IDeleteTaskUseCase

class DeleteTaskUseCaseImpl(
    private val taskRepository: ITaskRepository
) : IDeleteTaskUseCase {
    override suspend fun delete(task: Task) {
        taskRepository.delete(task)
    }
}
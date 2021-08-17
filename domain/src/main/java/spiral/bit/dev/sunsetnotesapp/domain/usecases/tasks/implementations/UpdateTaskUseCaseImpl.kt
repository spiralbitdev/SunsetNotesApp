package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations

import spiral.bit.dev.sunsetnotesapp.domain.models.Task
import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces.IUpdateTaskUseCase

class UpdateTaskUseCaseImpl(
    private val taskRepository: ITaskRepository
) : IUpdateTaskUseCase {
    override suspend fun update(task: Task) {
        taskRepository.update(task)
    }
}
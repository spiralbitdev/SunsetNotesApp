package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations

import spiral.bit.dev.sunsetnotesapp.domain.models.Task
import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces.IInsertTaskUseCase

class InsertTaskUseCaseImpl(
    private val taskRepository: ITaskRepository
) : IInsertTaskUseCase {
    override suspend fun insert(task: Task) {
        taskRepository.insert(task)
    }
}
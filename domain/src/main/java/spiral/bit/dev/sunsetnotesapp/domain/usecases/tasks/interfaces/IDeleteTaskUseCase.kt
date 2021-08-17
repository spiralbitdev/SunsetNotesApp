package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces

import spiral.bit.dev.sunsetnotesapp.domain.models.Task

interface IDeleteTaskUseCase {
    suspend fun delete(task: spiral.bit.dev.sunsetnotesapp.domain.models.Task)
}
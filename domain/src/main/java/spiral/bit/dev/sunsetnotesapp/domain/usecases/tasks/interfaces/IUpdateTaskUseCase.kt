package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces

import spiral.bit.dev.sunsetnotesapp.domain.models.Task

interface IUpdateTaskUseCase {
    suspend fun update(task: spiral.bit.dev.sunsetnotesapp.domain.models.Task)
}
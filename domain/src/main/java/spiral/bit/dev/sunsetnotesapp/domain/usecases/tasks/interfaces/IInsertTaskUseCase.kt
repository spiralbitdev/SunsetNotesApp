package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces

import spiral.bit.dev.sunsetnotesapp.domain.models.Task

interface IInsertTaskUseCase {
    suspend fun insert(task: spiral.bit.dev.sunsetnotesapp.domain.models.Task)
}
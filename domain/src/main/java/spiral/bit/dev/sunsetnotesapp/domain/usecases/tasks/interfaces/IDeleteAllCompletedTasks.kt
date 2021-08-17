package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces

interface IDeleteAllCompletedTasks {
    suspend fun deleteAllCompletedTasks()
}
package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces

interface IUpdateHideCompletedUseCase {
    suspend fun updateHideCompleted(hideCompleted: Boolean)
}
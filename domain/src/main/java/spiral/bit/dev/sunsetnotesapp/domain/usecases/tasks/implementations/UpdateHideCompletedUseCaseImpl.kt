package spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations

import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.interfaces.IUpdateHideCompletedUseCase

class UpdateHideCompletedUseCaseImpl(
    private val preferenceManager: IPreferenceManager
) : IUpdateHideCompletedUseCase {
    override suspend fun updateHideCompleted(hideCompleted: Boolean) {
        preferenceManager.updateHideCompleted(hideCompleted)
    }
}
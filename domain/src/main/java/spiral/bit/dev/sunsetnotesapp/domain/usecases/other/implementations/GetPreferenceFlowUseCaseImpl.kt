package spiral.bit.dev.sunsetnotesapp.domain.usecases.other.implementations

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.models.FilterPrefs
import spiral.bit.dev.sunsetnotesapp.domain.usecases.other.interfaces.IGetPreferenceFlowUseCase

class GetPreferenceFlowUseCaseImpl(
    private val preferenceManager: IPreferenceManager
) : IGetPreferenceFlowUseCase {
    override fun getPreferenceFlow(): Flow<FilterPrefs> {
        return preferenceManager.preferenceFlow
    }
}
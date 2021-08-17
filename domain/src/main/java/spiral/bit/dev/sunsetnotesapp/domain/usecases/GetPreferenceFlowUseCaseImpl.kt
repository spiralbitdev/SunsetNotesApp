package spiral.bit.dev.sunsetnotesapp.domain.usecases

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.models.FilterPrefs

class GetPreferenceFlowUseCaseImpl(
    private val preferenceManager: IPreferenceManager
) : IGetPreferenceFlowUseCase {
    override fun getPreferenceFlow(): Flow<FilterPrefs> {
        return preferenceManager.preferenceFlow
    }
}
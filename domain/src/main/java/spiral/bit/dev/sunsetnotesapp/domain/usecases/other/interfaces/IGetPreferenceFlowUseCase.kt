package spiral.bit.dev.sunsetnotesapp.domain.usecases.other.interfaces

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.FilterPrefs

interface IGetPreferenceFlowUseCase {
    fun getPreferenceFlow(): Flow<spiral.bit.dev.sunsetnotesapp.domain.models.FilterPrefs>
}
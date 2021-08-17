package spiral.bit.dev.sunsetnotesapp.domain.usecases

import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder

class UpdateSortOrderUseCaseImpl(
    private val preferenceManager: IPreferenceManager
) : IUpdateSortOrderUseCase {
    override suspend fun updateSortOrder(sortOrder: SortOrder) {
        preferenceManager.updateSortOrder(sortOrder)
    }
}
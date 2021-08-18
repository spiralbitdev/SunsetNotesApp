package spiral.bit.dev.sunsetnotesapp.domain.usecases.other.implementations

import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.usecases.other.interfaces.IUpdateSortOrderUseCase

class UpdateSortOrderUseCaseImpl(
    private val preferenceManager: IPreferenceManager
) : IUpdateSortOrderUseCase {
    override suspend fun updateSortOrder(sortOrder: SortOrder) {
        preferenceManager.updateSortOrder(sortOrder)
    }
}
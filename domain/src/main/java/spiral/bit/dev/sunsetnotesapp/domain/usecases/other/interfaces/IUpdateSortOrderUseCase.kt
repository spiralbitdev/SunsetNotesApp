package spiral.bit.dev.sunsetnotesapp.domain.usecases.other.interfaces

import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder

interface IUpdateSortOrderUseCase {
    suspend fun updateSortOrder(sortOrder: spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder)
}
package spiral.bit.dev.sunsetnotesapp.domain.managers

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.domain.models.FilterPrefs
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder

interface IPreferenceManager  {

    val preferenceFlow: Flow<FilterPrefs>

    suspend fun updateSortOrder(sortOrder: SortOrder)

    suspend fun updateHideCompleted(hideCompleted: Boolean)
}
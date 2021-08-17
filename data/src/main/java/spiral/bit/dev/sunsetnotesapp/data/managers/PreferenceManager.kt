package spiral.bit.dev.sunsetnotesapp.data.managers

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.models.FilterPrefs
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import java.io.IOException

class PreferenceManager(private val context: Context) : IPreferenceManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override val preferenceFlow: Flow<FilterPrefs>
        get() = context.dataStore.data
            .catch { ex ->
                if (ex is IOException) {
                    Log.d("ERROR", "Error reading or writing data. ${ex.localizedMessage}")
                    emit(emptyPreferences())
                }
            }
            .map { prefs ->
                val sortOrder = SortOrder.valueOf(
                    prefs[PreferencesKeys.SORT_ORDER_KEY] ?: SortOrder.BY_DATE.name
                )
                val hideCompleted = prefs[PreferencesKeys.HIDE_COMPLETED] ?: false
                FilterPrefs(sortOrder, hideCompleted)
            }

    override suspend fun updateSortOrder(sortOrder: SortOrder) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.SORT_ORDER_KEY] = sortOrder.name
        }
    }

    override suspend fun updateHideCompleted(hideCompleted: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
    }
}
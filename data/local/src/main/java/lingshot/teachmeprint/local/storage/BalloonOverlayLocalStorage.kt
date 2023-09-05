package lingshot.teachmeprint.local.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BalloonOverlayLocalStorage @Inject constructor(
    @ApplicationContext
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = BALLOON_OVERLAY
    )

    fun isBalloonOverlayVisible(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[BALLOON_OVERLAY_KEY] ?: true
        }
    }

    suspend fun saveAndHideBalloonOverlay() {
        context.dataStore.edit { preferences ->
            preferences[BALLOON_OVERLAY_KEY] = false
        }
    }

    companion object {
        private const val BALLOON_OVERLAY: String = "BALLOON_OVERLAY"
        private val BALLOON_OVERLAY_KEY = booleanPreferencesKey("BALLOON_OVERLAY_KEY")
    }
}

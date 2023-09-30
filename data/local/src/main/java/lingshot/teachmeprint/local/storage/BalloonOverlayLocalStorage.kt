/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lingshot.teachmeprint.local.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BalloonOverlayLocalStorage @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = BALLOON_OVERLAY,
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

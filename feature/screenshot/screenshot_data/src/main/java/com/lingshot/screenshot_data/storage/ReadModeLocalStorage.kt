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
package com.lingshot.screenshot_data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lingshot.screenshot_domain.model.ReadModeType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadModeLocalStorage @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = READ_MODE_DATA,
    )

    fun getMode(): Flow<ReadModeType?> {
        return context.dataStore.data.map { preferences ->
            val readModeType = preferences[READ_MODE_TYPE_KEY] ?: ReadModeType.STANDARD.id
            ReadModeType.from(readModeType)
        }
    }

    suspend fun saveMode(readModeType: ReadModeType) {
        context.dataStore.edit { preferences ->
            preferences[READ_MODE_TYPE_KEY] = readModeType.id
        }
    }

    companion object {
        private const val READ_MODE_DATA: String = "READ_MODE_DATA"
        private val READ_MODE_TYPE_KEY = intPreferencesKey("READ_MODE_TYPE_KEY")
    }
}

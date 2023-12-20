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
package com.lingshot.screenshot_data.repository

import com.lingshot.screenshot_data.storage.ReadModeLocalStorage
import com.lingshot.screenshot_domain.model.ReadModeType
import com.lingshot.screenshot_domain.repository.ReadModeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadModeRepositoryImpl @Inject constructor(
    private val readModeLocalStorage: ReadModeLocalStorage,
) : ReadModeRepository {

    override fun getMode(): Flow<ReadModeType?> {
        return readModeLocalStorage.getMode()
    }

    override suspend fun saveMode(readModeType: ReadModeType) {
        readModeLocalStorage.saveMode(readModeType)
    }
}

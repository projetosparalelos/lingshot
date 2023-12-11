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
package com.lingshot.analytics.helper

import com.google.firebase.analytics.FirebaseAnalytics.Event.SELECT_ITEM
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.lingshot.analytics.constant.LANGUAGE_FROM_TO_ITEM
import com.lingshot.languagechoice_domain.model.TranslateLanguageType
import com.lingshot.languagechoice_domain.repository.LanguageChoiceRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class AnalyticsEventHelper @Inject constructor(
    private val languageChoiceRepository: LanguageChoiceRepository,
) {

    private val firebaseAnalytics = Firebase.analytics

    suspend fun sendSelectItem(vararg params: Pair<String, String>) {
        try {
            val from = languageChoiceRepository.getLanguage(TranslateLanguageType.FROM).first()
            val to = languageChoiceRepository.getLanguage(TranslateLanguageType.TO).first()

            firebaseAnalytics.logEvent(SELECT_ITEM) {
                param(LANGUAGE_FROM_TO_ITEM, "${from?.languageCode}_${to?.languageCode}")
                params.forEach { (key, value) ->
                    param(key, value)
                }
            }
        } catch (e: Exception) {
            Timber.d(e.message)
        }
    }
}

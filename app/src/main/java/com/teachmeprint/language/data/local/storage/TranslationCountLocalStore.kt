package com.teachmeprint.language.data.local.storage

import com.orhanobut.hawk.Hawk
import javax.inject.Inject

class TranslationCountLocalStore @Inject constructor() {
    private var numCount = 1

    private fun getTranslationCount(): Int = Hawk.get(TRANSLATION_COUNT_KEY) ?: INITIAL_TRANSLATION_COUNT

    fun saveTranslationCount() {
        numCount++

        if (hasReachedMaxTranslationCount()) {
            deleteTranslationCount()
            return
        }
        Hawk.put(TRANSLATION_COUNT_KEY, numCount)
    }

    private fun deleteTranslationCount() {
        clearTranslationCount()
        Hawk.delete(TRANSLATION_COUNT_KEY)
    }

    fun hasReachedMaxTranslationCount(): Boolean {
        return getTranslationCount() == MAX_TRANSLATION_COUNT
    }

    private fun clearTranslationCount() {
        numCount = INITIAL_TRANSLATION_COUNT
    }

    companion object {
        const val TRANSLATION_COUNT_KEY = "TRANSLATION_COUNT_KEY"
        private const val INITIAL_TRANSLATION_COUNT = 1
        private const val MAX_TRANSLATION_COUNT = 15
    }
}

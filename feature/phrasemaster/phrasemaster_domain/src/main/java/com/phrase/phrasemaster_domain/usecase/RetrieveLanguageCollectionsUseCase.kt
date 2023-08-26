package com.phrase.phrasemaster_domain.usecase

import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusEmpty
import com.lingshot.domain.model.statusError
import com.lingshot.domain.model.statusSuccess
import com.phrase.phrasemaster_domain.model.LanguageCollectionDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RetrieveLanguageCollectionsUseCase(
    private val phraseCollectionRepository: PhraseCollectionRepository
) {
    operator fun invoke(): Flow<Status<List<LanguageCollectionDomain>>> = flow {
        try {
            val languageList = phraseCollectionRepository.getLanguageCollections()
            if (languageList.isNotEmpty()) {
                emit(statusSuccess(languageList))
            } else {
                emit(statusEmpty())
            }
        } catch (e: Exception) {
            Timber.e(e)
            if (e is CancellationException) throw e
            emit(statusError(e.message))
        }
    }
}
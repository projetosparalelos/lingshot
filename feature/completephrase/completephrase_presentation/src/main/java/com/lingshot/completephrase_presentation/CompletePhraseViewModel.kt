package com.lingshot.completephrase_presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.common.helper.TextToSpeechFacade
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CompletePhraseViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase,
    private val phraseCollectionRepository: PhraseCollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompletePhraseUiState())
    val uiState = _uiState.asStateFlow()

    private val textToSpeech = TextToSpeechFacade<String>(context) { status ->
        _uiState.update {
            it.copy(isSpeechActive = status.isLoadingStatus)
        }
    }

    fun handleEvent(completePhraseEvent: CompletePhraseEvent) {
        when (completePhraseEvent) {
            is CompletePhraseEvent.FetchTextToSpeech -> {
                fetchTextToSpeech(completePhraseEvent.text)
            }
        }
    }

    suspend fun fetchPhrasesByLanguageCollections(languageId: String?) {
        val phraseDomain = phraseCollectionRepository
            .getPhrasesByLanguageCollections(languageId ?: "")
        _uiState.update { it.copy(phrasesByLanguageCollectionsStatus = phraseDomain) }
    }

    private fun fetchTextToSpeech(text: String) {
        viewModelScope.launch {
            val languageCode = languageIdentifierUseCase(text)
            textToSpeech.speakText(text, languageCode)
        }
    }

    override fun onCleared() {
        super.onCleared()
        textToSpeech.shutdown()
    }
}

package com.lingshot.completephrase_presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingshot.common.helper.TextToSpeechFacade
import com.lingshot.common.helper.isLoadingStatus
import com.lingshot.completephrase_presentation.helper.AnswerSoundFacade
import com.lingshot.completephrase_presentation.ui.component.AnswerState
import com.lingshot.domain.model.Status
import com.lingshot.domain.model.statusDefault
import com.lingshot.domain.usecase.LanguageIdentifierUseCase
import com.phrase.phrasemaster_domain.model.PhraseDomain
import com.phrase.phrasemaster_domain.repository.PhraseCollectionRepository
import com.phrase.phrasemaster_domain.usecase.UpdatePhraseReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CompletePhraseViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val languageIdentifierUseCase: LanguageIdentifierUseCase,
    private val updatePhraseReviewUseCase: UpdatePhraseReviewUseCase,
    private val phraseCollectionRepository: PhraseCollectionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompletePhraseUiState())
    val uiState = _uiState.asStateFlow()

    private val answerSoundFacade = AnswerSoundFacade(context)

    private val textToSpeech = TextToSpeechFacade<String>(context) { status ->
        _uiState.update {
            it.copy(isSpeechActive = status.isLoadingStatus)
        }
    }

    fun handleEvent(completePhraseEvent: CompletePhraseEvent) {
        when (completePhraseEvent) {
            is CompletePhraseEvent.ClearState -> {
                clearState()
            }

            is CompletePhraseEvent.FillWord -> {
                fillWord(completePhraseEvent.word)
            }

            is CompletePhraseEvent.FetchAnswersFinished -> {
                fetchAnswersFinished()
            }

            is CompletePhraseEvent.FetchAnswerSound -> {
                fetchAnswerSound()
            }

            is CompletePhraseEvent.FetchTextToSpeech -> {
                fetchTextToSpeech(completePhraseEvent.text)
            }

            is CompletePhraseEvent.HideAnswerSheet -> {
                hideAnswerSheet()
            }

            is CompletePhraseEvent.ShowAnswerSheet -> {
                showAnswerSheet(completePhraseEvent.isAnswerCorrect)
            }

            is CompletePhraseEvent.ToggleTranslatedTextVisibility -> {
                toggleTranslatedTextVisibility()
            }

            is CompletePhraseEvent.UpdatePhrasePositionOnSuccess -> {
                updatePhrasePositionOnSuccess(
                    completePhraseEvent.languageId,
                    completePhraseEvent.phraseDomain
                )
            }

            is CompletePhraseEvent.UpdatePhrasePositionOnError -> {
                updatePhrasePositionOnError(completePhraseEvent.phraseDomain)
            }
        }
    }

    private fun clearState() {
        _uiState.update {
            it.copy(
                answerState = AnswerState(),
                isAnswerSheetVisible = false,
                isSpeechActive = true,
                isTranslatedTextVisible = false,
                wordToFill = "",
                updatePhraseInLanguageCollectionsStatus = statusDefault()
            )
        }
    }

    private fun fillWord(word: String) {
        _uiState.update {
            it.copy(wordToFill = word)
        }
    }

    private fun hideAnswerSheet() {
        _uiState.update {
            it.copy(
                isAnswerSheetVisible = false
            )
        }
    }

    private fun showAnswerSheet(isAnswerCorrect: Boolean) {
        _uiState.update {
            it.copy(
                isAnswerSheetVisible = true,
                answerState = it.answerState.copy(isSuccess = isAnswerCorrect)
            )
        }
    }

    private fun toggleTranslatedTextVisibility() {
        _uiState.update {
            it.copy(isTranslatedTextVisible = !it.isTranslatedTextVisible)
        }
    }

    private fun updatePhrasePositionOnSuccess(
        languageId: String?,
        phraseDomain: PhraseDomain
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    updatePhraseInLanguageCollectionsStatus =
                    updatePhraseReviewUseCase(languageId, phraseDomain)
                )
            }
        }
    }

    private fun updatePhrasePositionOnError(phraseDomain: PhraseDomain) {
        _uiState.update {
            it.copy(
                phrasesByLanguageCollections =
                it.phrasesByLanguageCollections.toMutableList().apply {
                    add(phraseDomain)
                }
            )
        }
    }

    suspend fun fetchPhrasesByLanguageCollections(languageId: String?) {
        delay(1.seconds)
        val phraseDomain = phraseCollectionRepository
            .getPhrasesByLanguageCollections(languageId ?: "")

        when (phraseDomain) {
            is Status.Success -> {
                _uiState.update {
                    it.copy(
                        phrasesByLanguageCollections = phraseDomain.data ?: emptyList(),
                        isLoading = false
                    )
                }
            }

            is Status.Empty -> {
                _uiState.update {
                    it.copy(
                        phrasesByLanguageCollections = emptyList(),
                        isLoading = false
                    )
                }
            }

            else -> Unit
        }
    }

    private fun fetchAnswersFinished() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAnswersFinished = true) }
        }.invokeOnCompletion {
            answerSoundFacade.playFinishedSound()
        }
    }

    private fun fetchAnswerSound() {
        if (_uiState.value.answerState.isSuccess) {
            answerSoundFacade.playSuccessSound()
        } else {
            answerSoundFacade.playErrorSound()
        }
    }

    private fun fetchTextToSpeech(text: String) {
        viewModelScope.launch {
            val languageCode = languageIdentifierUseCase(text)
            textToSpeech.speakText(text, languageCode)
        }
    }

    override fun onCleared() {
        super.onCleared()
        answerSoundFacade.cleanUpResources()
        textToSpeech.shutdown()
    }
}

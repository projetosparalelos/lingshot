package com.language.teachermetoon.feature.screenshot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.language.teachermetoon.core.helper.MutableLiveResource
import com.language.teachermetoon.core.helper.getLiveData
import com.language.teachermetoon.core.helper.launchResource
import com.language.teachermetoon.core.helper.loading
import com.language.teachermetoon.data.model.screenshot.entity.RequestBody
import com.language.teachermetoon.feature.screenshot.presentation.repository.ScreenShotRepository

class ScreenShotViewModel(private val screenShotRepository: ScreenShotRepository): ViewModel() {

    private val _response = MutableLiveResource<String>()
    val response by getLiveData(_response)

    fun sendPhrase(requestBody: RequestBody) {
        _response.loading()

        viewModelScope.launchResource(_response, {
            val response = screenShotRepository.getTranslatePhrase(requestBody)
            response.choices[0].text
        })
    }

    fun getLanguage(): String? {
        return screenShotRepository.getLanguage()
    }

    fun saveLanguage(languageSelected: String) {
        screenShotRepository.saveLanguage(languageSelected)
    }
}
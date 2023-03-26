package com.language.teachermetoon.core.di.module

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.language.teachermetoon.data.local.storage.LanguageLocalStorage
import com.language.teachermetoon.feature.screenshot.presentation.ScreenShotViewModel
import com.language.teachermetoon.feature.screenshot.repository.ScreenShotRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { ScreenShotRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { ScreenShotViewModel(androidContext(), get(), get(), get()) }
}

val dataBaseModule = module {
    single { LanguageLocalStorage() }
}

val librariesModule = module {
    single { TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS) }
    single { LanguageIdentification.getClient() }
}
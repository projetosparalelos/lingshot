package com.teachmeprint.language.core.di.module

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.teachmeprint.language.data.local.storage.LanguageLocalStorage
import com.teachmeprint.language.feature.screenshot.presentation.ScreenShotViewModel
import com.teachmeprint.language.feature.screenshot.presentation.ui.ScreenShotFloatingWindow
import com.teachmeprint.language.feature.screenshot.repository.ScreenShotRepository
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
    single { ScreenShotFloatingWindow(androidContext()) }
}
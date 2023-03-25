package com.language.teachermetoon.core.di.module

import com.language.teachermetoon.data.local.storage.LanguageLocalStorage
import com.language.teachermetoon.feature.screenshot.presentation.ScreenShotViewModel
import com.language.teachermetoon.feature.screenshot.presentation.repository.ScreenShotRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { ScreenShotRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { ScreenShotViewModel(get()) }
}

val dataBaseModule = module {
    single { LanguageLocalStorage() }
}
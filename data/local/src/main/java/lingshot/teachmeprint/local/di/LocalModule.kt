package lingshot.teachmeprint.local.di

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS
import com.lingshot.domain.repository.TextIdentifierRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import lingshot.teachmeprint.local.repository.TextIdentifierRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideScreenShotRepository(
        textRecognizer: TextRecognizer,
        languageIdentifier: LanguageIdentifier
    ): TextIdentifierRepository =
        TextIdentifierRepositoryImpl(textRecognizer, languageIdentifier)

    @Singleton
    @Provides
    fun provideTextRecognition() =
        TextRecognition.getClient(DEFAULT_OPTIONS)

    @Singleton
    @Provides
    fun provideLanguageIdentification() = LanguageIdentification.getClient()
}

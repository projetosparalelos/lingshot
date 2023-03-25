package com.language.teachermetoon.feature.screenshot.presentation.ui

import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.language.teachermetoon.*
import com.language.teachermetoon.core.helper.observeOnError
import com.language.teachermetoon.core.helper.observeOnLoading
import com.language.teachermetoon.core.helper.observeOnSuccess
import com.language.teachermetoon.core.helper.setLifecycleOwner
import com.language.teachermetoon.data.service.ScreenShotService.Companion.EXTRA_PATH_SCREEN_SHOT
import com.language.teachermetoon.databinding.ActivityScreenShotBinding
import com.language.teachermetoon.feature.screenshot.presentation.ScreenShotViewModel
import com.language.teachermetoon.data.model.screenshot.entity.RequestBody
import com.language.teachermetoon.core.util.snackBarAlert
import com.language.teachermetoon.data.model.screenshot.TypeActionEnum
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.util.*

class ScreenShotActivity : AppCompatActivity(), CropImageView.OnCropImageCompleteListener {

    private val binding by lazy {
        ActivityScreenShotBinding.inflate(layoutInflater)
    }

    private val imageUri by lazy {
        val message = intent.getStringExtra(EXTRA_PATH_SCREEN_SHOT) ?: ""
        Uri.fromFile(File(message))
    }

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private var textToSpeech: TextToSpeech? = null

    private val viewModel: ScreenShotViewModel by viewModel()

    private val cropImageOptions = CropImageOptions(
        guidelines = CropImageView.Guidelines.OFF,
        cornerShape = CropImageView.CropCornerShape.OVAL, showProgressBar = false
    )

    private lateinit var typeActionEnum: TypeActionEnum

    private val textIlegible = "There isn't any legible text."

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.cropImageScreenShot.setImageUriAsync(uri)
            setOptions()
        } else {
            Timber.d("No media selected")
        }
    }

    private val speechListener = object : UtteranceProgressListener() {
        override fun onStart(p0: String?) {
            lifecycleScope.launch {
                binding.animationScreenShotLoadingListen.isVisible = true
            }
        }

        override fun onDone(p0: String?) {
            lifecycleScope.launch {
                binding.animationScreenShotLoadingListen.isVisible = false
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onError(p0: String?) {
            lifecycleScope.launch {
                binding.animationScreenShotLoadingListen.isVisible = false
                binding.bottomNavigationScreenShot.snackBarAlert("Text to speech failed.")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigation()

        binding.cropImageScreenShot.setImageUriAsync(imageUri)
        binding.cropImageScreenShot.setOnCropImageCompleteListener(this)
        setOptions()

        viewModel.response.setLifecycleOwner(this)
            .observeOnSuccess { data ->
                binding.animationScreenShotLoadingTranslate.isVisible = false
                setupBalloon(data)
            }
            .observeOnError {
                binding.animationScreenShotLoadingTranslate.isVisible = false
                binding.bottomNavigationScreenShot.snackBarAlert("Text translate failed.")
            }
            .observeOnLoading {
                binding.animationScreenShotLoadingTranslate.isVisible = true
            }
        setupTextToSpeech()
    }

    private fun setupBalloon(phrase: String) {
        val balloon = Balloon.Builder(this)
            .setWidthRatio(0.90f)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText(phrase)
            .setTextSize(15f)
            .setArrowSize(10)
            .setMarginHorizontal(16)
            .setMarginBottom(8)
            .setPadding(12)
            .setCornerRadius(24f)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setBackgroundColor(ContextCompat.getColor(this, R.color.balloon_color))
            .setBalloonAnimation(BalloonAnimation.ELASTIC)
            .build()

        balloon.showAlignTop(binding.bottomNavigationScreenShot.findViewById(R.id.ic_translate))
    }
    private fun setupTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.SUCCESS) {
                binding.bottomNavigationScreenShot.snackBarAlert("Text to speech failed.")
            }
        }
        textToSpeech?.setOnUtteranceProgressListener(speechListener)
    }
    private fun getLanguages() {
        val language = Locale.getAvailableLocales().sortedBy { it.displayLanguage }
        val listaaa = arrayListOf<String>()

        language.forEach {
            if (!isLanguageInList(listaaa, it)) {
                listaaa.add(it.displayLanguage);
            }
        }

        var selectedLanguageIndex = listaaa.indexOf(viewModel.getLanguage())

        MaterialAlertDialogBuilder(this)
            .setTitle("Choose Language")
            .setSingleChoiceItems(listaaa.toTypedArray(), selectedLanguageIndex) { p0, p1 ->
                selectedLanguageIndex = p1
                viewModel.saveLanguage(listaaa[p1])

                p0.dismiss()
            }
            .setNegativeButton("Cancel") { _, _ -> }
            .show()
    }

    private fun isLanguageInList(list: List<String>?, locale: Locale): Boolean {
       return list?.any { it.equals(locale.displayLanguage, ignoreCase = true) } ?: false
    }

    private fun setupBottomNavigation() {
        val shapeDrawable = binding.bottomNavigationScreenShot.background as MaterialShapeDrawable

        shapeDrawable.shapeAppearanceModel = shapeDrawable.shapeAppearanceModel
            .toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, 90f)
            .build()

        binding.bottomNavigationScreenShot.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ic_translate -> {
                    if (!binding.animationScreenShotLoadingTranslate.isVisible) {
                        typeActionEnum = TypeActionEnum.TRANSLATE
                        binding.cropImageScreenShot.croppedImageAsync()
                    }
                }
                R.id.ic_listen -> {
                    if (!binding.animationScreenShotLoadingListen.isVisible) {
                        typeActionEnum = TypeActionEnum.LISTEN
                        binding.cropImageScreenShot.croppedImageAsync()
                    }
                }
                R.id.ic_reset_focus -> {
                    resetCrop()
                }
                R.id.ic_image_gallery -> {
                    pickMedia.launch(PickVisualMediaRequest(ImageOnly))
                }
                R.id.ic_language -> {
                    getLanguages()
                }
            }
            super.onOptionsItemSelected(item)
        }
    }

    private fun resetCrop() {
        binding.cropImageScreenShot.cropRect = Rect(0, 0, 0, 0)
    }

    private fun setOptions() {
        binding.cropImageScreenShot.cropRect = Rect(100, 300, 500, 700)
        binding.cropImageScreenShot.setImageCropOptions(cropImageOptions)
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        processImage(result.bitmap)
    }

    private fun processImage(imageBitmap: Bitmap?) {
        val image = imageBitmap?.let {
            InputImage.fromBitmap(it, 0)
        }

        image?.let {
            recognizer.process(it)
                .addOnSuccessListener { value ->
                    val textFormatted = value.text.checkTextAndFormat()
                    when (typeActionEnum) {
                        TypeActionEnum.TRANSLATE -> {
                            setupChatGPT(textFormatted)
                        }
                        TypeActionEnum.LISTEN -> {
                            languageIdentifier(textFormatted)
                        }
                    }
                }
                .addOnFailureListener {
                    binding.bottomNavigationScreenShot.snackBarAlert("Text recognizer failed.")
                }
        }
    }

    private fun languageIdentifier(phrase: String) {
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(phrase)
            .addOnSuccessListener { languageCode ->
                speakOut(phrase, languageCode)
            }
            .addOnFailureListener {
                binding.bottomNavigationScreenShot.snackBarAlert("Model couldn’t be loaded or other internal error.")
            }
    }

    private fun String.checkTextAndFormat(): String {
        val textFormatted = this.replace("\n", " ")
        return textFormatted.ifBlank { textIlegible }
    }

    private fun setupChatGPT(phrase: String) {
        viewModel.getLanguage()?.let { language ->
            val text = "Translate this into 1. ${language}:\\n\\n${phrase}\\n\\n1."

            val requestBody = RequestBody(
                "text-davinci-003", text, 100,
                0.6f, 1f, 0, 0, "\n"
            )

            if (phrase != textIlegible) {
                viewModel.sendPhrase(requestBody)
            } else {
                setupBalloon(phrase)
            }
        } ?: run {
            binding.bottomNavigationScreenShot.snackBarAlert("Select the language you want to translate.", "Add", true) {
                getLanguages()
            }
        }
    }

    private fun speakOut(phrase: String, languageCode: String) {
        textToSpeech?.setSpeechRate(0.7f)

        val languageLocale = if (languageCode == "und")
            Locale.US else Locale.forLanguageTag(languageCode)

        val result = textToSpeech?.setLanguage(languageLocale)

        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            binding.bottomNavigationScreenShot.snackBarAlert("This language is not supported.")
        }
        textToSpeech?.speak(phrase, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onDestroy() {
        if (textToSpeech != null) {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }

        super.onDestroy()
    }
}
package com.language.teachermetoon.feature.screenshot.presentation.ui

import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.language.teachermetoon.R
import com.language.teachermetoon.core.helper.StatusMessage.getErrorMessage
import com.language.teachermetoon.core.helper.observeOnError
import com.language.teachermetoon.core.helper.observeOnLoading
import com.language.teachermetoon.core.helper.observeOnSuccess
import com.language.teachermetoon.core.helper.setLifecycleOwner
import com.language.teachermetoon.core.util.snackBarAlert
import com.language.teachermetoon.data.model.screenshot.TypeActionEnum
import com.language.teachermetoon.data.model.screenshot.TypeActionEnum.LISTEN
import com.language.teachermetoon.data.model.screenshot.TypeActionEnum.TRANSLATE
import com.language.teachermetoon.data.service.ScreenShotService.Companion.EXTRA_PATH_SCREEN_SHOT
import com.language.teachermetoon.databinding.ActivityScreenShotBinding
import com.language.teachermetoon.feature.screenshot.presentation.ScreenShotViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import org.koin.androidx.viewmodel.ext.android.viewModel
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

    private val viewModel: ScreenShotViewModel by viewModel()

    private val cropImageOptions = CropImageOptions(
        guidelines = CropImageView.Guidelines.OFF,
        cornerShape = CropImageView.CropCornerShape.OVAL, showProgressBar = false
    )

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.cropImageScreenShot.setImageUriAsync(uri)
            setOptions()
        } else {
            Timber.d("No media selected")
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
                performActionWithLoadingIndicator {
                    setupBalloon(data)
                }
            }
            .observeOnError {
                performActionWithLoadingIndicator()
                binding.bottomNavigationScreenShot.snackBarAlert(getErrorMessage(it))
            }
            .observeOnLoading {
                performActionWithLoadingIndicator(true)
            }
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
                        viewModel.getLanguage()?.let {
                            performActionIndicator(TRANSLATE)
                        } ?: run  {
                            binding.bottomNavigationScreenShot.snackBarAlert("Select the language you want to translate.", "Add", true) {
                                getLanguages()
                            }
                        }
                    }
                }
                R.id.ic_listen -> {
                    performActionIndicator(LISTEN)
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

    private fun performActionIndicator(typeActionEnum: TypeActionEnum) {
        if (!binding.animationScreenShotLoadingTranslate.isVisible ||
            !binding.animationScreenShotLoadingListen.isVisible
        ) {
            viewModel.typeActionEnum = typeActionEnum
            binding.cropImageScreenShot.croppedImageAsync()
        }
    }
    private fun performActionWithLoadingIndicator(isVisible: Boolean = false, onActionTranslate: () -> Unit = {}) {
        when (viewModel.typeActionEnum) {
            TRANSLATE -> {
                binding.animationScreenShotLoadingTranslate.isVisible = isVisible
                onActionTranslate.invoke()
            }
            LISTEN -> {
                binding.animationScreenShotLoadingListen.isVisible = isVisible
            }
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
        viewModel.fetchTextRecognizer(result.bitmap)
    }

    override fun onDestroy() {
        viewModel.stopTextToSpeech()
        super.onDestroy()
    }
}
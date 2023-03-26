package com.teachmeprint.language.feature.screenshot.presentation.ui

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
import com.teachmeprint.language.R
import com.teachmeprint.language.core.helper.StatusMessage.getErrorMessage
import com.teachmeprint.language.core.helper.observeOnError
import com.teachmeprint.language.core.helper.observeOnLoading
import com.teachmeprint.language.core.helper.observeOnSuccess
import com.teachmeprint.language.core.helper.setLifecycleOwner
import com.teachmeprint.language.core.util.snackBarAlert
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum.LISTEN
import com.teachmeprint.language.data.model.screenshot.TypeIndicatorEnum.TRANSLATE
import com.teachmeprint.language.data.service.ScreenShotService.Companion.EXTRA_PATH_SCREEN_SHOT
import com.teachmeprint.language.databinding.ActivityScreenShotBinding
import com.teachmeprint.language.feature.screenshot.presentation.ScreenShotViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class ScreenShotActivity : AppCompatActivity(), CropImageView.OnCropImageCompleteListener {

    private val binding by lazy {
        ActivityScreenShotBinding.inflate(layoutInflater)
    }

    private val imageUriPath by lazy {
        val path = intent.getStringExtra(EXTRA_PATH_SCREEN_SHOT) ?: ""
        Uri.fromFile(File(path))
    }

    private val viewModel: ScreenShotViewModel by viewModel()

    private val requestPickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.cropImageScreenShot.setImageUriAsync(uri)
            setupImageCropOptions()
        } else {
            Timber.d(NO_MEDIA_SELECTED)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNavigation()
        setupCropImage()
        setupObservable()
    }

    private fun setupObservable() {
        viewModel.response.setLifecycleOwner(this)
            .observeOnSuccess { data ->
                performActionWithLoadingIndicator {
                    setupBalloonTranslate(data)
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

    private fun setupBalloonTranslate(phrase: String) =
        with(Balloon.Builder(this)) {
            setWidthRatio(0.90f)
            setHeight(BalloonSizeSpec.WRAP)
            setText(phrase)
            setTextSize(15f)
            setArrowSize(10)
            setMarginHorizontal(16)
            setMarginBottom(8)
            setPadding(12)
            setCornerRadius(24f)
            setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            setBackgroundColor(ContextCompat.getColor(this@ScreenShotActivity, R.color.balloon_translate_color))
            setBalloonAnimation(BalloonAnimation.ELASTIC)
            build().also {
                it.showAlignTop(binding.bottomNavigationScreenShot.findViewById(R.id.ic_translate))
            }
        }

    private fun setupDialogChooseLanguage() {
        var languageSelectedIndex = viewModel.getLanguageSelectedIndex()

        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.text_title_dialog_choose_language))
            .setSingleChoiceItems(viewModel.getLanguageList().toTypedArray(),
                languageSelectedIndex
            ) { _, index ->
                languageSelectedIndex = index
            }
            .setPositiveButton(getString(R.string.text_button_select_dialog_choose_language)) { dialog, _ ->
                viewModel.saveLanguage(viewModel.getLanguageList()[languageSelectedIndex])
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.text_button_dialog_cancel)) { _, _ -> }
            .show()
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
                        viewModel.getLanguage()?.let {
                            performActionIndicator(TRANSLATE)
                        } ?: run  {
                            binding.bottomNavigationScreenShot.snackBarAlert(
                                R.string.text_select_language_translate_message,
                                R.string.text_button_action_add_snack_bar,
                                true
                            ) {
                                setupDialogChooseLanguage()
                            }
                    }
                }
                R.id.ic_listen -> {
                    performActionIndicator(LISTEN)
                }
                R.id.ic_reset_focus -> {
                    resetImageCropReact()
                }
                R.id.ic_image_gallery -> {
                    requestPickMedia.launch(PickVisualMediaRequest(ImageOnly))
                }
                R.id.ic_language -> {
                    setupDialogChooseLanguage()
                }
            }
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupCropImage() {
        binding.cropImageScreenShot.setImageUriAsync(imageUriPath)
        binding.cropImageScreenShot.setOnCropImageCompleteListener(this)
        setupImageCropOptions()
    }
    private fun performActionIndicator(typeIndicatorEnum: TypeIndicatorEnum) {
        if (!binding.animationScreenShotLoadingTranslate.isVisible &&
            !binding.animationScreenShotLoadingListen.isVisible
        ) {
            viewModel.typeIndicatorEnum = typeIndicatorEnum
            binding.cropImageScreenShot.croppedImageAsync()
        }
    }
    private fun performActionWithLoadingIndicator(isVisible: Boolean = false, onActionTranslate: () -> Unit = {}) {
        when (viewModel.typeIndicatorEnum) {
            TRANSLATE -> {
                binding.animationScreenShotLoadingTranslate.isVisible = isVisible
                onActionTranslate.invoke()
            }
            LISTEN -> {
                binding.animationScreenShotLoadingListen.isVisible = isVisible
            }
        }
    }

    private fun setupImageCropOptions() {
        val cropImageOptions = CropImageOptions(
            guidelines = CropImageView.Guidelines.OFF,
            cornerShape = CropImageView.CropCornerShape.OVAL,
            showProgressBar = false
        )
        binding.cropImageScreenShot.cropRect = Rect(100, 300, 500, 700)
        binding.cropImageScreenShot.setImageCropOptions(cropImageOptions)
    }

    private fun resetImageCropReact() {
        binding.cropImageScreenShot.cropRect = Rect(0, 0, 0, 0)
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        viewModel.fetchTextRecognizer(result.bitmap)
    }

    override fun onDestroy() {
        viewModel.stopTextToSpeech()
        super.onDestroy()
    }

    companion object {
        private const val NO_MEDIA_SELECTED = "No media selected."
    }
}
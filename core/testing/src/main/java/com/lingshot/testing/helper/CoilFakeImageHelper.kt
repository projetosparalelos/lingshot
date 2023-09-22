@file:OptIn(ExperimentalCoilApi::class)

package com.lingshot.testing.helper

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import coil.Coil
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.test.FakeImageLoaderEngine

const val BASE_URL_COIL = "https://www.example.com/image.jpg"

fun startCoilFakeImage(context: Context) {
    val engine = FakeImageLoaderEngine.Builder()
        .intercept(data = BASE_URL_COIL, drawable = ColorDrawable(Color.GREEN))
        .build()
    val imageLoader = ImageLoader.Builder(context)
        .error(ColorDrawable(Color.RED))
        .components { add(engine) }
        .build()
    Coil.setImageLoader(imageLoader)
}

/*
 * Copyright 2023 Lingshot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

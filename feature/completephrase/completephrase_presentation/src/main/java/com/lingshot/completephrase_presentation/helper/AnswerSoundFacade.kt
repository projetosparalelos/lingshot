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

package com.lingshot.completephrase_presentation.helper

import android.content.Context
import android.media.MediaPlayer
import com.lingshot.completephrase_presentation.R

class AnswerSoundFacade(context: Context) {

    private val successSound = MediaPlayer.create(context, R.raw.verify_answer_success)
    private val errorSound = MediaPlayer.create(context, R.raw.verify_answer_error)
    private val finishedSound = MediaPlayer.create(context, R.raw.answers_finished)

    fun playSuccessSound() {
        successSound.start()
    }

    fun playErrorSound() {
        errorSound.start()
    }

    fun playFinishedSound() {
        finishedSound.start()
    }

    fun cleanUpResources() {
        successSound.release()
        errorSound.release()
        finishedSound.release()
    }
}

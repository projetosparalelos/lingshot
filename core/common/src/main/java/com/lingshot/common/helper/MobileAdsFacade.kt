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

package com.lingshot.common.helper

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.lingshot.common.BuildConfig.ADMOB_INTERSTITIAL_ID
import timber.log.Timber
import javax.inject.Inject

class MobileAdsFacade @Inject constructor() {

    fun setupInterstitialAds(activity: Activity) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            activity,
            ADMOB_INTERSTITIAL_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.w(adError.message)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    activity.showInterstitialAd(interstitialAd)
                    setupInterstitialAdListener(interstitialAd)
                }
            },
        )
    }

    private fun Activity.showInterstitialAd(interstitialAd: InterstitialAd) {
        interstitialAd.show(this)
    }

    private fun setupInterstitialAdListener(interstitialAd: InterstitialAd) {
        interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Timber.w(adError.message)
            }
        }
    }
}

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
package com.lingshot.home_presentation.helper

import android.app.Activity
import android.content.Context
import com.lingshot.home_presentation.BuildConfig
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.entitlements.QEntitlement
import com.qonversion.android.sdk.dto.products.QProduct
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback
import com.qonversion.android.sdk.listeners.QonversionProductsCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Qonversion.hasPremiumEntitlement() = withContext(Dispatchers.IO) {
    suspendCoroutine { continuation ->
        this@hasPremiumEntitlement.checkEntitlements(
            object : QonversionEntitlementsCallback {
                override fun onError(error: QonversionError) {
                    continuation.resume(false)
                }

                override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                    val premiumEntitlement = entitlements[PREMIUM_OFFERING]
                    val hasPremiumEntitlement = premiumEntitlement?.isActive == true

                    Timber.d("premiumEntitlement: $premiumEntitlement")
                    continuation.resume(hasPremiumEntitlement || BuildConfig.DEBUG)
                }
            },
        )
    }
}

suspend fun Qonversion.getPremiumProduct() =
    withContext(Dispatchers.IO) {
        suspendCoroutine { continuation ->
            this@getPremiumProduct.products(
                object : QonversionProductsCallback {
                    override fun onError(error: QonversionError) {
                        Timber.d("Error $error")
                        continuation.resume(null)
                    }

                    override fun onSuccess(products: Map<String, QProduct>) {
                        continuation.resume(products)
                    }
                },
            )
        }
    }

fun Qonversion.purchaseProduct(
    context: Context,
    product: QProduct,
    onFinish: (QEntitlement?) -> Unit,
) = if (context is Activity) {
    this.purchase(
        context,
        product,
        object : QonversionEntitlementsCallback {
            override fun onError(error: QonversionError) {
                Timber.d("error purchase $error")
                onFinish(null)
            }

            override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                Timber.d("success purchase $entitlements")
                onFinish(entitlements[PREMIUM_OFFERING])
            }
        },
    )
} else {
    onFinish(null)
}

suspend fun Qonversion.restorePurchases() =
    suspendCoroutine { continuation ->
        this.restore(
            object : QonversionEntitlementsCallback {
                override fun onError(error: QonversionError) {
                    continuation.resume(null)
                }

                override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                    continuation.resume(entitlements[PREMIUM_OFFERING])
                }
            },
        )
    }

private const val PREMIUM_OFFERING = "premium"
const val LINGSHOT_PAYMENT_ID = "lingshot_payment_id"

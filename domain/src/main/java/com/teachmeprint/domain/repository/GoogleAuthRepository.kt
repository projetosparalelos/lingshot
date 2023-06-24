package com.teachmeprint.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.teachmeprint.domain.model.SignInResult
import com.teachmeprint.domain.model.UserDomain

interface GoogleAuthRepository {
    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): SignInResult
    suspend fun signOut()
    fun getSignedInUser(): UserDomain?
}
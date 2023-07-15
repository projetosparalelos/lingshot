package com.lingshot.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.lingshot.domain.model.SignInResult
import com.lingshot.domain.model.UserDomain

interface GoogleAuthRepository {
    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): SignInResult
    suspend fun signOut()
    fun getSignedInUser(): UserDomain?
}

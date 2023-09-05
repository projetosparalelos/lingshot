package com.lingshot.domain.repository

import kotlinx.coroutines.flow.Flow

interface BalloonOverlayRepository {

    fun isBalloonOverlayVisible(): Flow<Boolean>

    suspend fun saveAndHideBalloonOverlay()
}

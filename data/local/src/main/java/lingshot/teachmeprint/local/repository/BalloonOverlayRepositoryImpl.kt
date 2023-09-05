package lingshot.teachmeprint.local.repository

import com.lingshot.domain.repository.BalloonOverlayRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import lingshot.teachmeprint.local.storage.BalloonOverlayLocalStorage

class BalloonOverlayRepositoryImpl @Inject constructor(
    private val balloonOverlayLocalStorage: BalloonOverlayLocalStorage
) : BalloonOverlayRepository {

    override fun isBalloonOverlayVisible(): Flow<Boolean> {
        return balloonOverlayLocalStorage.isBalloonOverlayVisible()
    }

    override suspend fun saveAndHideBalloonOverlay() {
        balloonOverlayLocalStorage.saveAndHideBalloonOverlay()
    }
}

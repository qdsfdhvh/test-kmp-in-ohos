package internal

import androidx.compose.runtime.MonotonicFrameClock
import ohos.systemDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal object OhosAnimationFrameClock : MonotonicFrameClock {
    override suspend fun <R> withFrameNanos(
        onFrame: (frameTimeNanos: Long) -> R,
    ): R = suspendCoroutine { continuation ->
        systemDateTime.getCurrentTime(isNano = true) { durationNanos ->
            val result = onFrame(durationNanos)
            continuation.resume(result)
        }
    }
}
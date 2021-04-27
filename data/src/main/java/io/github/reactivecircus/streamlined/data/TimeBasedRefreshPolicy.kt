package io.github.reactivecircus.streamlined.data

import io.github.reactivecircus.store.ext.RefreshPolicy
import io.github.reactivecircus.store.ext.RefreshScope
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A [RefreshPolicy] implementation which determines whether data should be refreshed
 * for a [RefreshScope] based on the duration since a refresh was last recorded for the same [RefreshScope].
 * Once a refresh has been recorded for a [RefreshScope], refresh won't be necessary for new requests
 * until [expiration] time has passed where the [expiration] is updated / extended each time a refresh
 * is recorded, effectively debouncing the expiration of a data set for the given [RefreshScope].
 */
class TimeBasedRefreshPolicy(
    private val expiration: Duration = DEFAULT_EXPIRATION,
    private val timeSource: TimeSource = TimeSource.Monotonic
) : RefreshPolicy {

    init {
        require(expiration.isPositive()) { "Expiration for refresh policy must be positive." }
    }

    private val lock = Mutex()

    private val refreshLog = mutableMapOf<RefreshScope, TimeMark>()

    override suspend fun shouldRefresh(refreshScope: RefreshScope): Boolean {
        return lock.withLock {
            val expirationMark = refreshLog[refreshScope]
            expirationMark == null || (expirationMark + expiration).hasPassedNow()
        }
    }

    override suspend fun onRefreshed(refreshScope: RefreshScope) {
        lock.withLock {
            val expirationMark = refreshLog[refreshScope]
            if (expirationMark != null) {
                refreshLog[refreshScope] = expirationMark + expirationMark.elapsedNow()
            } else {
                refreshLog[refreshScope] = timeSource.markNow()
            }
        }
    }

    /**
     * Clears the refresh logs for all [RefreshScope]s which means [shouldRefresh] will return true
     * for all [RefreshScope]s until [onRefreshed] is invoked again.
     */
    suspend fun reset() {
        lock.withLock { refreshLog.clear() }
    }

    companion object {
        val DEFAULT_EXPIRATION = Duration.minutes(5)
    }
}

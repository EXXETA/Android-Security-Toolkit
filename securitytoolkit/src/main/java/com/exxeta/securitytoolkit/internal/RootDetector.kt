package com.exxeta.securitytoolkit.internal

import android.content.Context
import com.exxeta.securitytoolkit.ThreatException
import com.exxeta.securitytoolkit.ThreatNotPresent
import com.exxeta.securitytoolkit.ThreatPresent
import com.exxeta.securitytoolkit.ThreatStatus
import com.scottyab.rootbeer.RootBeer

/**
 * A Detector object for Root
 */
internal object RootDetector {

    /**
     * Exposes Root check API
     *
     * @param context - required for root check
     */
    fun threatDetected(context: Context): ThreatStatus = try {
        if (isRooted(context)) {
            ThreatPresent
        } else {
            ThreatNotPresent
        }
    } catch (e: Throwable) {
        ThreatException(e)
    }

    /**
     * Performs check for Root
     *
     * @param context - application context
     * @throws [RuntimeException] if failed to check
     * @return true if rooted
     */
    @Throws(RuntimeException::class)
    private fun isRooted(context: Context): Boolean {
        try {
            val rootbeer = RootBeer(context).also { it.setLogging(false) }
            return rootbeer.isRooted
        } catch (e: Throwable) {
            throw RuntimeException("Could not check for root: $e")
        }
    }
}

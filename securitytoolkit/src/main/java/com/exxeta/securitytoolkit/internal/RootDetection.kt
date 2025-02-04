package com.exxeta.securitytoolkit.internal

import android.content.Context
import com.scottyab.rootbeer.RootBeer

/**
 * A Detector object for Root
 */
internal object RootDetection {

    /**
     * Exposes Root check API
     *
     * @param context - required for root check
     * @throws [RuntimeException] if failed to check
     * @return true if root detected
     */
    @Throws(RuntimeException::class)
    fun threatDetected(context: Context): Boolean = isRooted(context)

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
            return RootBeer(context).isRooted
        } catch (e: Throwable) {
            throw RuntimeException("Could not check for root: $e")
        }
    }
}

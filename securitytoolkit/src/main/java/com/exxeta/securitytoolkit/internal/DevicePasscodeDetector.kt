package com.exxeta.securitytoolkit.internal

import android.app.KeyguardManager
import android.content.Context
import com.exxeta.securitytoolkit.ThreatException
import com.exxeta.securitytoolkit.ThreatNotPresent
import com.exxeta.securitytoolkit.ThreatPresent
import com.exxeta.securitytoolkit.ThreatStatus

/**
 * A Detector object for device passcode presence
 */
internal object DevicePasscodeDetector {

    /**
     * Exposes public API to detect device passcode
     */
    fun threatDetected(context: Context): ThreatStatus = try {
        if (isSecure(context)) {
            ThreatNotPresent
        } else {
            ThreatPresent
        }
    } catch (e: Throwable) {
        ThreatException(e)
    }

    private fun isSecure(context: Context): Boolean {
        val keyguardManager =
            context.getSystemService(
                Context.KEYGUARD_SERVICE,
            ) as KeyguardManager
        return keyguardManager.isDeviceSecure
    }
}

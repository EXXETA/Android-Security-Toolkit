package com.exxeta.securitytoolkit.internal

import android.app.KeyguardManager
import android.content.Context

/**
 * A Detector object for device passcode presence
 */
internal object DevicePasscodeDetection {

    /**
     * Exposes public API to detect device passcode
     *
     * @return true if device passcode is absent
     */
    fun threatDetected(context: Context): Boolean {
        val keyguardManager =
            context.getSystemService(
                Context.KEYGUARD_SERVICE,
            ) as KeyguardManager
        return !keyguardManager.isDeviceSecure
    }
}

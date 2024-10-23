package com.exxeta.securitytoolkit

import android.content.Context
import com.exxeta.securitytoolkit.internal.EmulatorDetector
import com.exxeta.securitytoolkit.internal.HooksDetection
import com.exxeta.securitytoolkit.internal.RootDetection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Thread detection center exposes the API to check for threats. Use the
 * variables
 * - [areRootPrivilegesDetected]: to check for root
 * - [areHooksDetected]: to check for injection (hooking) tweaks
 * - [isSimulatorDetected]: to check if the app is running in a simulated environment
 *
 * Better (safer) way of detection threats is to use the [threats] Flow.
 * Subscribe to it and collect values - threats that were detected
 *
 * @property context - Application Context, required to check for root
 */
class ThreatDetectionCenter(private val context: Context) {

    /**
     * Performs check for root
     *
     * @throws [RuntimeException] if root check failed
     */
    val areRootPrivilegesDetected: Boolean
        @Throws(RuntimeException::class)
        get() = RootDetection.threatDetected(context)

    /**
     * Performs check for injection (hooking) libraries
     */
    val areHooksDetected: Boolean
        get() = HooksDetection.threatDetected()

    /**
     * Performs check for Emulator / Simulator environment
     */
    val isSimulatorDetected: Boolean
        get() = EmulatorDetector.threatDetected()

    /**
     * Defines a better way to detect threats. Will contain every threat that
     * is detected
     */
    val threats: Flow<Threat>
        get() = flow {
            try {
                if (RootDetection.threatDetected(context)) {
                    emit(Threat.ROOT_PRIVILEGES)
                }
            } catch (_: Throwable) {
                // ignore here for now, cause is not clear for this exception
            }
            if (HooksDetection.threatDetected()) {
                emit(Threat.HOOKS)
            }
            if (EmulatorDetector.threatDetected()) {
                emit(Threat.SIMULATOR)
            }
        }

    /**
     * Defines all possible threats, that can be reported via the flow
     */
    public enum class Threat {
        ROOT_PRIVILEGES,
        HOOKS,
        SIMULATOR
    }
}
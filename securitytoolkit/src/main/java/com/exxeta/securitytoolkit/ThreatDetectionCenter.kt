package com.exxeta.securitytoolkit

import android.content.Context
import com.exxeta.securitytoolkit.internal.AppSignatureDetection
import com.exxeta.securitytoolkit.internal.DevicePasscodeDetection
import com.exxeta.securitytoolkit.internal.EmulatorDetector
import com.exxeta.securitytoolkit.internal.HardwareSecurityDetection
import com.exxeta.securitytoolkit.internal.HooksDetection
import com.exxeta.securitytoolkit.internal.RootDetection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Thread detection center exposes the API to check for threats. Use the
 * variables
 * - [areRootPrivilegesDetected]: to check for root
 * - [areHooksDetected]: to check for injection (hooking) tweaks
 * - [isSimulatorDetected]: to check if the app is running in a simulated
 * environment
 * - [isDeviceWithoutPasscodeDetected]: to check if device is protected with a
 * passcode
 * - [isHardwareProtectionUnavailable]: to check if device can use
 * hardware-backed cryptography
 * - [hasAppSignatureMissmatch]: extract current app signature and match against
 * expected one
 *
 * Better (safer) way of detection threats is to use the [threats] Flow.
 * Subscribe to it and collect values - threats that were detected
 *
 * @property context - Application Context, required for multiple checks
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
     * Performs check for Device Passcode presence
     * Returns `false`, when device is **unprotected**
     */
    val isDeviceWithoutPasscodeDetected: Boolean
        get() = DevicePasscodeDetection.threatDetected(context)

    /**
     * Performs check for hardware backed encryption presence
     * Returns `false`, when device does **not** support hardware encryption
     *
     * @throws [RuntimeException] if any operations with KeyStore have failed
     */
    val isHardwareProtectionUnavailable: Boolean
        get() = HardwareSecurityDetection.threatDetected(context)

    /**
     * Performs check of app signature (signing certificate SHA-256 hash)
     * Returns `false`, if expected and current signature does not match
     *
     * More: https://stackoverflow.com/questions/38558623/how-to-find-signature-of-apk-file#61807617
     *
     * > When distributing via PlayStore, use the SHA-256 Hash from Play Console
     *
     * > Otherwise find App signature with apktool
     *
     * @throws [RuntimeException] if failed to extract current signature
     */
    fun hasAppSignatureMissmatch(expectedSignature: String): Boolean =
        AppSignatureDetection.threatDetected(context, expectedSignature)

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
            if (DevicePasscodeDetection.threatDetected(context)) {
                emit(Threat.DEVICE_WITHOUT_PASSCODE)
            }
            try {
                if (HardwareSecurityDetection.threatDetected(context)) {
                    emit(Threat.HARDWARE_PROTECTION_UNAVAILABLE)
                }
            } catch (_: Throwable) {
                // TODO
            }
            // TODO: Add app signature check, after ThreatDetectionCenter refactoring
        }

    /**
     * Defines all possible threats, that can be reported via the flow
     */
    public enum class Threat {
        ROOT_PRIVILEGES,
        HOOKS,
        SIMULATOR,
        DEVICE_WITHOUT_PASSCODE,
        HARDWARE_PROTECTION_UNAVAILABLE,
    }
}

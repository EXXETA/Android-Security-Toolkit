package com.exxeta.securitytoolkit

import android.content.Context
import com.exxeta.securitytoolkit.internal.AppSignatureDetector
import com.exxeta.securitytoolkit.internal.DevicePasscodeDetector
import com.exxeta.securitytoolkit.internal.EmulatorDetector
import com.exxeta.securitytoolkit.internal.HardwareSecurityDetector
import com.exxeta.securitytoolkit.internal.HooksDetector
import com.exxeta.securitytoolkit.internal.RootDetector
import java.io.Closeable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Thread detection center exposes the API to check for threats. You can use the
 * variables
 * - [rootPrivilegesStatus]: to check for root
 * - [hooksStatus]: to check for injection (hooking) tweaks
 * - [emulatorStatus]: to check if the app is running in a simulated environment
 * - [devicePasscodeStatus]: to check if device is protected with a passcode
 * - [hardwareCryptographyStatus]: to check if device can use
 * hardware-backed cryptography
 * - [appSignatureStatus]: extract current app signature and match against
 * expected one
 *
 * Better (safer) way of detection threats is to use the [threatReports] Flow.
 * Subscribe to it and collect values - reports of all current detected threats
 *
 * > Important: As the checks are running in background, you should call [close] when you
 * don't need detection anymore
 *
 * @property context - Application Context, required for multiple checks
 * @property coroutineDispatcher - dispatcher to use when creating coroutines
 * @property configuration - additional required configuration to detect threats
 */
class ThreatDetectionCenter(
    private val context: Context,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val configuration: ThreatDetectionConfiguration =
        ThreatDetectionConfiguration(),
) : Closeable {

    private var job: Job? = null

    class ThreatDetectionConfiguration(
        val periodicDetectionCycle: Duration = 60.seconds,
        val expectedSignature: String? = null,
    )

    val threatReports: StateFlow<ThreatReport>
        get() {
            job = CoroutineScope(coroutineDispatcher).launch {
                CoroutineScope(coroutineDispatcher).launch {
                    flow {
                        while (true) {
                            emit(RootDetector.threatDetected(context))
                            delay(configuration.periodicDetectionCycle)
                        }
                    }.collect { updateReport { copy(rootPrivileges = it) } }
                }

                CoroutineScope(coroutineDispatcher).launch {
                    flow {
                        while (true) {
                            emit(HooksDetector.threatDetected())
                            delay(configuration.periodicDetectionCycle)
                        }
                    }.collect { updateReport { copy(hooks = it) } }
                }

                CoroutineScope(coroutineDispatcher).launch {
                    val status = EmulatorDetector.threatDetected()
                    updateReport { copy(emulator = status) }
                }

                CoroutineScope(coroutineDispatcher).launch {
                    flow {
                        while (true) {
                            emit(DevicePasscodeDetector.threatDetected(context))
                            delay(configuration.periodicDetectionCycle)
                        }
                    }.collect { updateReport { copy(devicePasscode = it) } }
                }

                CoroutineScope(coroutineDispatcher).launch {
                    val status =
                        HardwareSecurityDetector.threatDetected(context)
                    updateReport { copy(hardwareCryptography = status) }
                }

                CoroutineScope(coroutineDispatcher).launch {
                    updateReport {
                        copy(
                            appSignature = AppSignatureDetector.threatDetected(
                                context,
                                configuration.expectedSignature,
                            ),
                        )
                    }
                }
            }
            return threatFlow.asStateFlow()
        }

    private val threatFlow = MutableStateFlow(ThreatReport())

    override fun close() {
        job?.cancelChildren()
        job?.cancel()
    }

    // MARK: - Function API

    /**
     * Performs check for root
     */
    val rootPrivilegesStatus: ThreatStatus
        get() = RootDetector.threatDetected(context)

    /**
     * Performs check for injection (hooking) libraries
     */
    val hooksStatus: ThreatStatus
        get() = HooksDetector.threatDetected()

    /**
     * Performs check for Emulator / Simulator environment
     */
    val emulatorStatus: ThreatStatus
        get() = EmulatorDetector.threatDetected()

    /**
     * Performs check for Device Passcode presence
     */
    val devicePasscodeStatus: ThreatStatus
        get() = DevicePasscodeDetector.threatDetected(context)

    /**
     * Performs check for hardware backed encryption presence
     */
    val hardwareCryptographyStatus: ThreatStatus
        get() = HardwareSecurityDetector.threatDetected(context)

    /**
     * Performs check of app signature (signing certificate SHA-256 hash)
     *
     * More: https://stackoverflow.com/questions/38558623/how-to-find-signature-of-apk-file#61807617
     *
     * > When distributing via PlayStore, use the SHA-256 Hash from Play Console
     *
     * > Otherwise find App signature with apktool
     */
    val appSignatureStatus: ThreatStatus
        get() = AppSignatureDetector.threatDetected(
            context,
            configuration.expectedSignature,
        )

    // MARK: - Private API

    private fun updateReport(updateFunction: ThreatReport.() -> ThreatReport) {
        threatFlow.update { updateFunction(it) }
    }
}

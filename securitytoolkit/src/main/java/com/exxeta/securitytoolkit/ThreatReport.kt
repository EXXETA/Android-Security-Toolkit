package com.exxeta.securitytoolkit

sealed class ThreatStatus
data object ThreatNotChecked : ThreatStatus()
data object ThreatNotPresent : ThreatStatus()
data object ThreatPresent : ThreatStatus()
class ThreatException(val exception: Throwable) : ThreatStatus()

/**
 * Describes a status of all checks at once
 *
 * @property rootPrivileges - status of root check
 * @property hooks - status of hooks check
 * @property emulator - status of emulator check
 * @property devicePasscode - status of passcode check
 * @property hardwareCryptography - status of hardware check
 * @property appSignature - status of signature check
 */
data class ThreatReport(
    val rootPrivileges: ThreatStatus = ThreatNotChecked,
    val hooks: ThreatStatus = ThreatNotChecked,
    val emulator: ThreatStatus = ThreatNotChecked,
    val devicePasscode: ThreatStatus = ThreatNotChecked,
    val hardwareCryptography: ThreatStatus = ThreatNotChecked,
    val appSignature: ThreatStatus = ThreatNotChecked,
)

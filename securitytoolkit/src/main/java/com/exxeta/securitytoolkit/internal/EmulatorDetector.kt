package com.exxeta.securitytoolkit.internal

import android.os.Build
import android.os.Debug
import com.exxeta.securitytoolkit.ThreatException
import com.exxeta.securitytoolkit.ThreatNotPresent
import com.exxeta.securitytoolkit.ThreatPresent
import com.exxeta.securitytoolkit.ThreatStatus
import java.io.File

/**
 * A Detector object for Emulators
 */
internal object EmulatorDetector {

    /**
     * Exposes public API to detect emulators
     */
    fun threatDetected(): ThreatStatus = try {
        if (hasSuspiciousBuildConfiguration() ||
            hasSuspiciousFiles() ||
            Debug.isDebuggerConnected()
        ) {
            ThreatPresent
        } else {
            ThreatNotPresent
        }
    } catch (e: Throwable) {
        ThreatException(e)
    }

    private fun hasSuspiciousBuildConfiguration(): Boolean = (
        Build.MANUFACTURER.contains("Genymotion") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.lowercase().contains("droid4x") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu") ||
            Build.HARDWARE.contains("vbox86") ||
            Build.FINGERPRINT.startsWith("generic") ||
            Build.PRODUCT.contains("sdk") ||
            Build.PRODUCT.contains("google_sdk") ||
            Build.PRODUCT.contains("sdk_x86") ||
            Build.PRODUCT.contains("vbox86p") ||
            Build.HARDWARE.lowercase().contains("nox") ||
            Build.PRODUCT.lowercase().contains("nox") ||
            Build.BOARD.lowercase().contains("nox") ||
            (
                Build.BRAND.startsWith("generic") &&
                    Build.DEVICE.startsWith(
                        "generic",
                    )
                )
        )

    private fun hasSuspiciousFiles(): Boolean {
        val NOX_FILES = arrayOf("fstab.nox", "init.nox.rc", "ueventd.nox.rc")
        val ANDY_FILES = arrayOf("fstab.andy", "ueventd.andy.rc")
        val PIPES = arrayOf("/dev/socket/qemud", "/dev/qemu_pipe")
        val GENY_FILES =
            arrayOf("/dev/socket/genyd", "/dev/socket/baseband_genyd")
        val X86_FILES = arrayOf(
            "ueventd.android_x86.rc",
            "x86.prop",
            "ueventd.ttVM_x86.rc",
            "init.ttVM_x86.rc",
            "fstab.ttVM_x86",
            "fstab.vbox86",
            "init.vbox86.rc",
            "ueventd.vbox86.rc",
        )
        return (
            checkFiles(GENY_FILES) ||
                checkFiles(ANDY_FILES) ||
                checkFiles(NOX_FILES) ||
                checkFiles(X86_FILES) ||
                checkFiles(PIPES)
            )
    }

    private fun checkFiles(targets: Array<String>): Boolean {
        for (pipe in targets) {
            val file = File(pipe)
            if (file.exists()) {
                return true
            }
        }
        return false
    }
}

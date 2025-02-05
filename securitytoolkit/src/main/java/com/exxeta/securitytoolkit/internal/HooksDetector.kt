package com.exxeta.securitytoolkit.internal

import com.exxeta.securitytoolkit.ThreatException
import com.exxeta.securitytoolkit.ThreatNotPresent
import com.exxeta.securitytoolkit.ThreatPresent
import com.exxeta.securitytoolkit.ThreatStatus
import java.io.BufferedReader
import java.io.FileReader
import java.net.InetSocketAddress
import java.net.Socket

/**
 * A Detector object for Hooks
 */
internal object HooksDetector {

    /**
     * Exposes public API to check for hooks
     */
    fun threatDetected(): ThreatStatus = try {
        if (isFridaServerListening() || isFridaLoaded()) {
            ThreatPresent
        } else {
            ThreatNotPresent
        }
    } catch (e: Throwable) {
        ThreatException(e)
    }

    /**
     * Will check if frida is listening
     */
    private fun isFridaServerListening(): Boolean = try {
        val address = InetSocketAddress("127.0.0.1", 27042)
        val socket = Socket()
        socket.connect(address, 200)
        true
    } catch (e: Throwable) {
        false
    }

    /**
     * Will check if frida is loaded in /proc/self/maps
     */
    private fun isFridaLoaded(): Boolean {
        try {
            val br = BufferedReader(FileReader("/proc/self/maps"))
            val hasFrida = br.lines().anyMatch {
                it.lowercase().contains("frida")
            }
            br.close()
            return hasFrida
        } catch (e: Throwable) {
            return false
        }
    }
}

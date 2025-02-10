package com.exxeta.securitytoolkit.internal

import android.os.Debug
import java.io.FileReader
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


/**
 * A Detector object for debugger
 */
internal object DebuggerDetection {

    private val adbPorts = (5555..5585 step 2).map { it.toString() }
    private val adbConsolePorts = (5554..5584 step 2).map { it.toString() }

    /**
     * Exposes public API to detect debugger
     *
     * @return
     * - `true` if debugger is detected.
     * - `false` if no debugger is detected.
     * - `null` if the detection process did not produce a definitive result.
     * This could happen due to system limitations, lack of required
     * permissions, or other undefined conditions.
     */
    fun threatDetected(): Boolean? {
        val isTracerPidSet = isTracerPidSet() ?: return null
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger() || isAdbPortListening() || isTracerPidSet
    }

    private fun isTracerPidSet(): Boolean? {
        var isTracerPidSet: Boolean? = false
        try {
            FileReader("/proc/self/status").buffered().use { br ->
                isTracerPidSet = br.lines().anyMatch {
                    it.startsWith("TracerPid:") && it.split(":")[1].trim() != "0"
                }
            }
        } catch (e: IOException) {
            isTracerPidSet = null
        }
        return isTracerPidSet
    }

    private fun isAdbPortListening(): Boolean {
        return adbPorts.any { isPortInUse(it.toInt()) }
    }

    private fun isPortInUse(port: Int): Boolean {
        var isPortInUse = false
        try {
            val address = InetSocketAddress("127.0.0.1", port)
            Socket().use { socket ->
                socket.connect(address, 200)
                isPortInUse = true
            }
        } catch (e: Exception) {
            isPortInUse = false
        }
        return isPortInUse
    }

}

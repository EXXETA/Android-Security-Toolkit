package com.exxeta.securitytoolkit.internal

import android.os.Debug
import java.io.FileReader
import java.net.InetSocketAddress
import java.net.Socket


/**
 * A Detector object for debugger
 */
internal object DebuggerDetection {

    private val adbPorts = (5555..5585 step 2)

    /**
     * Exposes public API to detect debugger
     *
     * @return
     * - `true` if debugger is detected.
     * - `false` if no debugger is detected.
     */
    fun threatDetected(): Boolean {
        return Debug.isDebuggerConnected() || Debug.waitingForDebugger() || isAdbPortListening() || isTracerPidSet()
    }

    private fun isTracerPidSet(): Boolean = try {
        FileReader("/proc/self/status").buffered().use { br ->
            br.lines().anyMatch {
                it.startsWith("TracerPid:") && it.split(":")[1].trim() != "0"
            }
        }
    } catch (e: Throwable) {
        false
    }

    private fun isAdbPortListening(): Boolean {
        return adbPorts.any { isPortInUse(it) }
    }

    private fun isPortInUse(port: Int): Boolean = try {
        Socket().use { socket ->
            socket.connect(InetSocketAddress("127.0.0.1", port), 200)
        }
        true
    } catch (e: Throwable) {
        false
    }

}

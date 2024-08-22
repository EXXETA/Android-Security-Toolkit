package com.exxeta.securitytoolkit.internal

import android.content.Context
import java.io.BufferedReader
import java.io.FileReader
import java.net.InetSocketAddress
import java.net.Socket

internal object HooksDetection {

    @Throws(RuntimeException::class)
    fun threatDetected(): Boolean {
        return isFridaServerListening() || isFridaLoaded()
    }

    /**
     * Will check if frida is listening
     */
    private fun isFridaServerListening(): Boolean {
        return try {
            val address = InetSocketAddress("127.0.0.1", 27042)
            val socket = Socket()
            socket.connect(address, 200)
            true
        } catch (e: Throwable) {
            false
        }
    }

    /**
     * Will check if frida is loaded in /proc/self/maps
     */
    @Throws(RuntimeException::class)
    private fun isFridaLoaded(): Boolean {
        try {
            val br = BufferedReader(FileReader("/proc/self/maps"))
            val hasFrida = br.lines().anyMatch {
                it.lowercase().contains("frida")
            }
            br.close()
            return hasFrida
        } catch (e: Throwable) {
            throw RuntimeException("Could not check for maps: $e")
        }
    }
}
package com.exxeta.securitytoolkit.internal

import android.app.Application
import android.content.Context
import com.scottyab.rootbeer.RootBeer

internal object RootDetection {

    @Throws(RuntimeException::class)
    fun threatDetected(context: Context): Boolean {
        return isRooted(context)
    }

    @Throws(RuntimeException::class)
    private fun isRooted(context: Context): Boolean {
        try {
            return RootBeer(context).isRooted()
        } catch (e: Throwable) {
            throw RuntimeException("Could not get check root: $e")
        }
    }
}
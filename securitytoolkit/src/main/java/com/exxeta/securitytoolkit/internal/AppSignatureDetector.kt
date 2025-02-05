package com.exxeta.securitytoolkit.internal

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.exxeta.securitytoolkit.ThreatException
import com.exxeta.securitytoolkit.ThreatNotChecked
import com.exxeta.securitytoolkit.ThreatNotPresent
import com.exxeta.securitytoolkit.ThreatPresent
import com.exxeta.securitytoolkit.ThreatStatus
import java.security.MessageDigest

/**
 * A Detector object for app signature differences
 */
internal object AppSignatureDetector {
    private const val SHA_256 = "SHA-256"

    /**
     * Exposes public API to assert expected app signature
     */
    fun threatDetected(context: Context, expectedHash: String?): ThreatStatus =
        try {
            when (expectedHash) {
                null -> ThreatNotChecked
                getSigningCertHash(context) -> ThreatNotPresent
                else -> ThreatPresent
            }
        } catch (e: Throwable) {
            ThreatException(e)
        }

    /**
     * Will return the SHA-256 Hash of the signing certificate
     */
    @Throws(RuntimeException::class)
    private fun getSigningCertHash(context: Context): String? {
        try {
            val info: PackageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES,
            )
            val signers = info.signingInfo?.apkContentsSigners
            if (signers.isNullOrEmpty()) {
                return null
            }
            val signature = signers.first()

            val sha = MessageDigest.getInstance(SHA_256)
            sha.update(signature.toByteArray())
            val hash = sha.digest()

            return bytesToHex(hash)
        } catch (e: Throwable) {
            throw RuntimeException("Could not get signing cert hash: $e")
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val builder = StringBuilder()
        for (aByte in bytes) {
            builder.append(String.format("%02x", aByte))
        }
        return builder.toString()
    }
}

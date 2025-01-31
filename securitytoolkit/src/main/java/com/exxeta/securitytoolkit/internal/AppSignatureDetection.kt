package com.exxeta.securitytoolkit.internal

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.security.MessageDigest

/**
 * A Detector object for app signature differences
 */
internal object AppSignatureDetection {
    private const val SHA_256 = "SHA-256"

    /**
     * Exposes public API to assert expected app signature
     *
     * @return true if app signature same as expected
     * @throws [RuntimeException] if failed to extract current signature
     */
    fun threatDetected(context: Context, expectedHash: String): Boolean =
        getSigningCertHash(context) != expectedHash

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

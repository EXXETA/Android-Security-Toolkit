package com.exxeta.securitytoolkit.internal

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import com.exxeta.securitytoolkit.ThreatException
import com.exxeta.securitytoolkit.ThreatNotPresent
import com.exxeta.securitytoolkit.ThreatPresent
import com.exxeta.securitytoolkit.ThreatStatus
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory

/**
 * A Detector object for device passcode presence
 */
internal object HardwareSecurityDetector {
    private const val ANDROID_KEY_STORE_NAME = "AndroidKeyStore"

    /**
     * Exposes public API to detect hardware security
     */
    internal fun threatDetected(context: Context): ThreatStatus = try {
        if (!isEncrypted(context) || !supportHardwareBackedCryptography()) {
            ThreatPresent
        } else {
            ThreatNotPresent
        }
    } catch (e: Throwable) {
        ThreatException(e)
    }

    // https://source.android.com/docs/security/features/encryption
    private fun isEncrypted(context: Context): Boolean {
        val devicePolicyManager = context
            .getSystemService(
                Context.DEVICE_POLICY_SERVICE,
            ) as DevicePolicyManager

        val status = devicePolicyManager.storageEncryptionStatus
        return setOf(
            DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE,
            DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE_PER_USER,
        ).contains(status)
    }

    // https://developer.android.com/privacy-and-security/keystore
    private fun supportHardwareBackedCryptography(): Boolean = try {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
        keyStore.load(null)

        val alias = getNewAlias(keyStore)
        val result = createSecretKey(alias).let(::checkSecretKey)
        keyStore.deleteEntry(alias)

        result
    } catch (e: Throwable) {
        throw RuntimeException("Could not check hardware encryption: $e")
    }

    private fun getNewAlias(keyStore: KeyStore): String =
        (100000..999999).random().toString().repeat(5).let {
            if (keyStore.containsAlias(it)) {
                getNewAlias(keyStore)
            } else {
                it
            }
        }

    private fun createSecretKey(keyAlias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE_NAME,
        )
        val keySpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
        )
            .setKeySize(256)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setRandomizedEncryptionRequired(true)
            .setUserAuthenticationRequired(true)
            .setIsStrongBoxBacked(true)
            .build()
        keyGenerator.init(keySpec)
        return keyGenerator.generateKey()
    }

    private fun checkSecretKey(key: SecretKey): Boolean {
        val keyInfo = SecretKeyFactory.getInstance(key.algorithm)
            .getKeySpec(key, KeyInfo::class.java) as KeyInfo

        return checkKeyInfo(keyInfo)
    }

    private fun checkKeyInfo(keyInfo: KeyInfo): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setOf(
                KeyProperties.SECURITY_LEVEL_TRUSTED_ENVIRONMENT,
                KeyProperties.SECURITY_LEVEL_STRONGBOX,
            ).contains(keyInfo.securityLevel)
        } else {
            keyInfo.isInsideSecureHardware
        }
}

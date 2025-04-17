package com.example.shop_pay_supermarket_androidapp.features.auth

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.*
import java.security.spec.ECGenParameterSpec

class CryptoUtils {
    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val RSA_KEY_ALIAS = "user_rsa_key"
        private const val EC_KEY_ALIAS = "user_ec_key"

        /**
         * Generates both RSA and EC key pairs and returns encoded public keys
         */
        fun generateKeyPairs(): KeyPairResult {
            val rsaKeyPair = generateRsaKeyPair()
            val ecKeyPair = generateEcKeyPair()

            return KeyPairResult(
                rsaPublicKey = Base64.encodeToString(rsaKeyPair.public.encoded, Base64.DEFAULT),
                ecPublicKey = Base64.encodeToString(ecKeyPair.public.encoded, Base64.DEFAULT)
            )
        }

        private fun generateRsaKeyPair(): KeyPair {
            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA,
                ANDROID_KEYSTORE
            )

            val parameterSpec = KeyGenParameterSpec.Builder(
                RSA_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).apply {
                setKeySize(2048)
                setDigests(KeyProperties.DIGEST_SHA256)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            }.build()

            keyPairGenerator.initialize(parameterSpec)
            return keyPairGenerator.generateKeyPair()
        }

        private fun generateEcKeyPair(): KeyPair {
            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC,
                ANDROID_KEYSTORE
            )

            val parameterSpec = KeyGenParameterSpec.Builder(
                EC_KEY_ALIAS,
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
            ).apply {
                setDigests(KeyProperties.DIGEST_SHA256)
                setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
            }.build()

            keyPairGenerator.initialize(parameterSpec)
            return keyPairGenerator.generateKeyPair()
        }
    }

    data class KeyPairResult(
        val rsaPublicKey: String,
        val ecPublicKey: String
    )
}
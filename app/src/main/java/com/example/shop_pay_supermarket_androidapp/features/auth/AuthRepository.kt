package com.example.shop_pay_supermarket_androidapp.features.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

class AuthRepository(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }

    suspend fun registerUser(
        name: String,
        username: String,
        email: String,
        password: String,
        creditCard: String
    ): RegistrationResult = withContext(Dispatchers.IO) {
        try {
            // 1. Generate cryptographic key pairs
            val keyPairs = CryptoUtils.generateKeyPairs()

            // 2. Prepare registration data
            val registrationData = JSONObject().apply {
                put("name", name)
                put("username", username)
                put("email", email)
                put("credit_card", creditCard)
                put("rsa_public_key", keyPairs.rsaPublicKey)
                put("ec_public_key", keyPairs.ecPublicKey)
            }

            // 3. Send to the server (example implementation)
            val url = URL("http://10.0.2.2:8080/api/register")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            connection.outputStream.use { os ->
                os.write(registrationData.toString().toByteArray())
            }

            // 4. Process the response
            if (connection.responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonResponse = JSONObject(response)

                val userId = jsonResponse.getString("user_id")
                val supermarketPublicKey = jsonResponse.getString("supermarket_public_key")

                // 5. Store the values
                storeUserData(userId, password, supermarketPublicKey)

                RegistrationResult.Success
            } else {
                val errorResponse = connection.errorStream.bufferedReader().use { it.readText() }
                RegistrationResult.Error("Registration failed: $errorResponse")
            }
        } catch (e: Exception) {
            RegistrationResult.Error("Registration failed: ${e.message}")
        }
    }

    private fun storeUserData(userId: String, password: String, supermarketPublicKey: String) {
        prefs.edit().apply {
            putString("USER_ID", userId)
            putString("PASSWORD_HASH", hashPassword(password))
            putString("SUPERMARKET_PUBLIC_KEY", supermarketPublicKey)
            apply()
        }
    }

    private fun hashPassword(password: String): String {
        // In a real app, use a proper password hashing algorithm like BCrypt
        // This is a simplified example
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(password.toByteArray())
        return Base64.encodeToString(digest, Base64.DEFAULT)
    }

    sealed class RegistrationResult {
        object Success : RegistrationResult()
        data class Error(val message: String) : RegistrationResult()
    }
}
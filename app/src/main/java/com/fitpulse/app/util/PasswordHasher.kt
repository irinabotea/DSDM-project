package com.fitpulse.app.util

import java.security.MessageDigest
import java.security.SecureRandom

object PasswordHasher {

    /** Generates a random salt encoded as a hex string. */
    fun generateSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.toHex()
    }

    /** Hashes [password] with [salt] using SHA-256, returning a hex string. */
    fun hash(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val input = (salt + password).toByteArray(Charsets.UTF_8)
        return digest.digest(input).toHex()
    }

    private fun ByteArray.toHex(): String =
        joinToString("") { "%02x".format(it) }
}

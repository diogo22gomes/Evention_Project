package com.example.evention.data.remote.authentication

import UserPreferences
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.nio.charset.StandardCharsets

@Composable
fun RequireAuth(
    navController: NavController,
    userPreferences: UserPreferences,
    content: @Composable () -> Unit
) {
    val token = remember { userPreferences.getToken() }
    val userId = remember { userPreferences.getUserId() }

    val isLoggedIn = remember(token, userId) {
        token != null && userId != null && isTokenValid(token)
    }

    LaunchedEffect(isLoggedIn) {
        if (!isLoggedIn) {
            delay(100)
            navController.navigate("signIn") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    if (isLoggedIn) {
        content()
    }
}

fun isTokenValid(token: String): Boolean {
    try {
        val parts = token.split(".")
        if (parts.size != 3) return false

        val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8)
        val payload = JSONObject(payloadJson)

        val exp = payload.optLong("exp", 0L)
        if (exp == 0L) return false

        val now = System.currentTimeMillis() / 1000

        return exp > now
    } catch (e: Exception) {
        return false
    }
}
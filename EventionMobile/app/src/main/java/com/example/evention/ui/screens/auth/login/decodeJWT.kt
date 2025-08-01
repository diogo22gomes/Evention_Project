package com.example.evention.ui.screens.auth.login

import android.util.Base64
import org.json.JSONObject

fun decodeJWT(token: String): JSONObject {
    val parts = token.split(".")
    if (parts.size != 3) throw IllegalArgumentException("Token inválido")

    val payload = parts[1]
    val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
    val json = String(decodedBytes, charset("UTF-8"))

    return JSONObject(json)
}
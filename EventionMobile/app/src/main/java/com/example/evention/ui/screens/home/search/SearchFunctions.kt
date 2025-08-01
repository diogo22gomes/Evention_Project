package com.example.evention.ui.screens.home.search

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

fun fetchCoordinates(context: Context, query: String, cameraPositionState: CameraPositionState) {
    CoroutineScope(Dispatchers.Main).launch {
        try {
            val result = withTimeout(10_000) {
                withContext(Dispatchers.IO) {
                    Geocoder(context).getFromLocationName(query, 1)
                }
            }

            if (result?.isNotEmpty() == true) {
                val latLng = LatLng(result[0].latitude, result[0].longitude)

                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                    durationMs = 1000
                )
            } else {
                Toast.makeText(context, "Local não encontrado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}


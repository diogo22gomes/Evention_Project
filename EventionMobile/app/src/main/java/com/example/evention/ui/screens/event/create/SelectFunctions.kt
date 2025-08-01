package com.example.evention.ui.screens.event.create

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
import java.util.Locale

fun fetchSelectCoordinates(
    context: Context,
    locationName: String,
    cameraPositionState: CameraPositionState,
    onResult: (LatLng) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(locationName, 1)

            if (!addresses.isNullOrEmpty()) {

                val location = addresses[0]
                val latLng =
                    com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)

                withContext(Dispatchers.Main) {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    onResult(latLng)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


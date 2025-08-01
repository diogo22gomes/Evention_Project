package com.example.evention.ui.screens.event.create

import CustomCreateEventTextField
import UserPreferences
import android.location.Geocoder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evention.R
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.evention.ui.components.MenuComponent
import com.example.evention.ui.components.createEvent.CustomDateRangeTextField
import com.example.evention.ui.components.createEvent.LocationSelectorField
import com.example.evention.ui.components.home.FilterButtonWithDateRange
import com.example.evention.utils.isNetworkAvailable
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Salvar LatLng com rememberSaveable
val LatLngSaver: Saver<LatLng?, *> = Saver(
    save = { listOf(it?.latitude, it?.longitude) },
    restore = {
        val lat = it[0] as? Double
        val lng = it[1] as? Double
        if (lat != null && lng != null) LatLng(lat, lng) else null
    }
)

@Composable
fun CreateEventScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

    val viewModel: CreateEventViewModel = viewModel(
        factory = CreateEventViewModelFactory(userPreferences)
    )

    val createEventState by viewModel.createEventState.collectAsState()

    val selectedStartDate = rememberSaveable { mutableStateOf<Date?>(null) }
    val selectedEndDate = rememberSaveable { mutableStateOf<Date?>(null) }
    val showDatePicker = rememberSaveable { mutableStateOf(false) }

    val eventName = rememberSaveable { mutableStateOf("") }
    val eventDescription = rememberSaveable { mutableStateOf("") }
    val eventPrice = rememberSaveable { mutableStateOf("") }

    val selectedLocation = rememberSaveable(stateSaver = LatLngSaver) { mutableStateOf<LatLng?>(null) }
    val addressText = rememberSaveable { mutableStateOf("") }

    val showMessage = remember { mutableStateOf(false) }
    val messageText = remember { mutableStateOf("") }
    val isSuccessMessage = remember { mutableStateOf(true) }

    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    val selectedImageUri by viewModel.selectedImageUri.collectAsState()

    LaunchedEffect(Unit) {
        isConnected = isNetworkAvailable(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setSelectedImageUri(it) }
    }

    // localização da SelectLocationScreen
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val navLocation = currentBackStackEntry?.savedStateHandle?.get<LatLng>("selectedLocation")
    LaunchedEffect(navLocation) {
        navLocation?.let {
            selectedLocation.value = it
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = buildString {
                    append(address.thoroughfare ?: "")
                    if (address.subThoroughfare != null) append(", ${address.subThoroughfare}")
                    if (address.postalCode != null) append(", ${address.postalCode}")
                    if (address.locality != null) append(", ${address.locality}")
                }
                addressText.value = fullAddress
                currentBackStackEntry.savedStateHandle.remove<LatLng>("selectedLocation")
            }
        }
    }

    // logica mensagens
    LaunchedEffect(createEventState) {
        when (val state = createEventState) {
            is CreateEventState.Success -> {
                messageText.value = "Event created successfully!"
                isSuccessMessage.value = true
                showMessage.value = true

                delay(3000)
                showMessage.value = false

                navController.navigate("userEvents") {
                    popUpTo("create") { inclusive = true }
                }
            }

            is CreateEventState.Error -> {
                messageText.value = state.message
                isSuccessMessage.value = false
                showMessage.value = true

                delay(4000)
                showMessage.value = false
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            MenuComponent(currentPage = "Create", navController = navController)
        }
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            // Floating card
            AnimatedVisibility(
                visible = showMessage.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .zIndex(2f)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSuccessMessage.value) Color(0xFF66BB6A) else Color(0xFFD32F2F)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = if (isSuccessMessage.value) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = messageText.value,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }
            }

            if(isConnected){
                // Main
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 25.dp, vertical = 18.dp)
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TitleComponent("Create Event", false, navController)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        if (selectedImageUri != null) {
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Selected Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.default_event),
                                contentDescription = "Default Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Button(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(width = 100.dp, height = 34.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.blue_camera),
                                contentDescription = "Blue camera icon",
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CHANGE",
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                color = EventionBlue,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    CustomDateRangeTextField(
                        labelText = "Event Duration",
                        startDate = selectedStartDate.value,
                        endDate = selectedEndDate.value,
                        formatter = formatter
                    ) { start, end ->
                        selectedStartDate.value = start
                        selectedEndDate.value = end
                    }

                    if (showDatePicker.value) {
                        FilterButtonWithDateRange { startMillis, endMillis ->
                            if (startMillis != null && endMillis != null) {
                                selectedStartDate.value = Date(startMillis)
                                selectedEndDate.value = Date(endMillis)
                            }
                            showDatePicker.value = false
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    CustomCreateEventTextField("Event Name", eventName.value, description = false) {
                        eventName.value = it
                    }

                    CustomCreateEventTextField("Description", eventDescription.value, description = true) {
                        eventDescription.value = it
                    }

                    LocationSelectorField(
                        labelText = "Event Location",
                        selectedLocation = selectedLocation.value,
                        displayAddress = addressText.value,
                        onClick = { navController.navigate("selectLocation") }
                    )

                    CustomCreateEventTextField("Price", eventPrice.value, isPrice = true) {
                        eventPrice.value = it
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Button(
                        onClick = {
                            val name = eventName.value
                            val description = eventDescription.value
                            val priceString = eventPrice.value.replace("€", "").trim()
                            val price = priceString.toDoubleOrNull()
                            val start = selectedStartDate.value
                            val end = selectedEndDate.value
                            val location = selectedLocation.value

                            if (name.isNotBlank() && description.isNotBlank() && start != null && end != null && location != null && price != null) {
                                viewModel.createEvent(name, description, start, end, price, location, context)
                            } else {
                                Toast.makeText(context, "Incorrect fields", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EventionBlue)
                    ) {
                        if (createEventState is CreateEventState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text("Create Event", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "No connection",
                            tint = Color.Gray,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No internet connection",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                isConnected = isNetworkAvailable(context)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EventionBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Try again")
                        }
                    }
                }
            }


        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    EventionTheme {
        val navController = rememberNavController()
        CreateEventScreen(navController = navController)
    }
}
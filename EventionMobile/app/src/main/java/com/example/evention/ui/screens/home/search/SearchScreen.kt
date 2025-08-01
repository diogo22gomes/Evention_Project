import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evention.ui.theme.EventionTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Date
import com.google.maps.android.compose.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import android.Manifest
import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.evention.ui.components.MenuComponent
import com.example.evention.ui.screens.home.search.fetchCoordinates
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.evention.model.Event
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.utils.isNetworkAvailable
import com.google.gson.Gson

private const val REQUEST_LOCATION_PERMISSION = 1

@SuppressLint("MissingPermission")
fun moveToCurrentLocation(context: Context, cameraPositionState: CameraPositionState) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
        return
    }

    CoroutineScope(Dispatchers.Main).launch {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    cameraPositionState.move(
                        update = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f),
                    )
                } else {
                    Toast.makeText(context, "Localização não disponível.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e("MapLocation", "Erro ao obter localização: ${e.message}")
                Toast.makeText(context, "Erro ao obter localização.", Toast.LENGTH_SHORT).show()
            }
    }
}

@Composable
fun SearchScreen(events: List<Event>, modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }
    val query = remember { mutableStateOf("") }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(38.7169, -9.1399), 10f)
    }
    val selectedEventIndex = remember { mutableStateOf(-1) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            MenuComponent(
                currentPage = "Search",
                navController = navController
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if(isConnected){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.3f)
                        .background(Color.Gray)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        events.forEach { event ->
                            event.addressEvents.forEach { address ->
                                address.routes.forEach { route ->
                                    Marker(
                                        state = MarkerState(position = LatLng(route.latitude, route.longitude)),
                                        title = event.name,
                                        snippet = "${address.localtown}, ${address.road} ${address.roadNumber}",
                                        onClick = {
                                            val eventJson = Uri.encode(Gson().toJson(event))
                                            navController.navigate("eventDetails/$eventJson")
                                            true
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(30))
                            .clip(RoundedCornerShape(20))
                            .background(Color.White)
                            .padding(horizontal = 9.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Gray,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        TextField(
                            value = query.value,
                            onValueChange = { query.value = it },
                            placeholder = {
                                Text(
                                    "Find for city, localtown",
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )

                        IconButton(onClick = {
                            fetchCoordinates(context, query.value, cameraPositionState)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        }
                        IconButton(onClick = {
                            moveToCurrentLocation(context, cameraPositionState)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(events.size) { eventIndex ->
                        val event = events[eventIndex]
                        val address = event.addressEvents.firstOrNull()
                        val route = address?.routes?.firstOrNull()

                        val locationText = if (address != null) {
                            "${address.postCode}, ${address.road} ${address.roadNumber}"
                        } else {
                            "Localização desconhecida"
                        }

                        val isSelected = selectedEventIndex.value == eventIndex

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    route?.let {
                                        val latLng = LatLng(it.latitude, it.longitude)
                                        CoroutineScope(Dispatchers.Main).launch {
                                            cameraPositionState.animate(
                                                update = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                                            )
                                        }
                                    }

                                    if (selectedEventIndex.value == eventIndex) {
                                        val eventJson = Uri.encode(Gson().toJson(event))
                                        navController.navigate("eventDetails/$eventJson")
                                    } else {
                                        selectedEventIndex.value = eventIndex
                                    }
                                }
                        ) {
                            EventRow(
                                imageUrl = event.eventPicture!!,
                                title = event.name,
                                location = locationText,
                                date = event.createdAt,
                                isSelected = isSelected
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
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

@Composable
fun EventRow(imageUrl: String, title: String, location: String, date: Date, isSelected: Boolean = false) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient {
                getUnsafeOkHttpClient(userPreferences)
            }
            .build()
    }
    val imageUrl = "https://10.0.2.2:5010/event${imageUrl}"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp).border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) EventionBlue else Color.Transparent,
                shape = RoundedCornerShape(13.dp)
            ),

        shape = RoundedCornerShape(13.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp, top = 3.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = imageUrl,
                    imageLoader = imageLoader,
                    contentDescription = "Event Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onError = { var hasError = true }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(top = 5.dp))
                Text(
                    text = "30 Mai 2025",
                    color = Color(0xFF0081FF),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Text(
                    text = location,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Previeww() {
    EventionTheme {
        val navController = rememberNavController()
        var events = listOf<Event>()
        SearchScreen(events, navController = navController)
    }
}

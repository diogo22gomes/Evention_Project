package com.example.evention.ui.screens.home.details

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.evention.mock.MockData
import com.example.evention.model.Event
import com.example.evention.ui.components.eventDetails.EventDescription
import com.example.evention.ui.components.eventDetails.EventDetailsRow
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.evention.ui.screens.ticket.TicketScreenViewModel
import com.google.gson.Gson
import UserPreferences
import getUnsafeOkHttpClient
import com.example.evention.R

fun formatDate(date: Date): String {
    val localDate = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy", Locale.ENGLISH)
    return localDate.format(formatter)
}

fun formatTime(date: Date): String {
    val localDateTime = date.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    val formatter = DateTimeFormatter.ofPattern("EEEE, hh:mm a", Locale.ENGLISH)
    return localDateTime.format(formatter)
}

@Composable
fun EventDetails(
    eventDetails: Event,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TicketScreenViewModel = viewModel(),
    viewModelE: EventDetailsViewModel = viewModel()
) {
    val ticketId by viewModel.ticketId.collectAsState()
    var navigateToPayment by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient {
                getUnsafeOkHttpClient(userPreferences)
            }
            .build()
    }

    LaunchedEffect(ticketId) {
        if (!ticketId.isNullOrBlank() && navigateToPayment) {
            val eventJson = Uri.encode(Gson().toJson(eventDetails))
            navController.navigate("payment/${eventJson}/$ticketId")
            navigateToPayment = false
            viewModel.clearCreateResult()
        }
    }

    var hasError by remember { mutableStateOf(false) }

    eventDetails.let { event ->
        val user by viewModelE.user.collectAsState()
        val location by viewModelE.location.collectAsState()

        LaunchedEffect(event.userId) {
            if (viewModelE.user.value == null) {
                viewModelE.loadUserById(event.userId)
            }
        }

        LaunchedEffect(event.addressEvents.firstOrNull()?.localtown) {
            val address = event.addressEvents.firstOrNull()
            if (address?.localtown != null && viewModelE.location.value == null) {
                viewModelE.loadLocationById(address.localtown)
            }
        }

        val imageUrl = "https://10.0.2.2:5010/event${event.eventPicture}"
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    if (hasError || event.eventPicture == null) {
                        Image(
                            painter = painterResource(id = R.drawable.default_event),
                            contentDescription = "Event Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        AsyncImage(
                            model = imageUrl,
                            imageLoader = imageLoader,
                            contentDescription = "Event Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                            onError = { hasError = true }
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(24.dp)
                            .size(28.dp)
                            .clickable {
                                navController.popBackStack()
                            }
                    )
                }

                Text(
                    text = event.name,
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 25.dp, vertical = 18.dp)
                )

                EventDetailsRow(
                    icon = Icons.Outlined.DateRange,
                    contentDescription = "Calendar",
                    title = formatDate(event.startAt),
                    subtitle = formatTime(event.startAt)
                )

                val address = event.addressEvents.firstOrNull()
                val isLoadingLocation = location == null

                EventDetailsRow(
                    icon = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    title = if (isLoadingLocation) {
                        address?.localtown ?: "Unknown localtown"
                    } else {
                        location!!.localtown
                    },
                    subtitle = address?.road ?: "Unknown road"
                )

                val isLoadingUser = user == null

                EventDetailsRow(
                    icon = Icons.Filled.Person,
                    contentDescription = "Person",
                    title = if (isLoadingUser) "Loading..." else user!!.username,
                    subtitle = "",
                    onClick = if (!isLoadingUser) {
                        {
                            val userJson = Uri.encode(Gson().toJson(user))
                            navController.navigate("profile?userJson=$userJson")
                        }
                    } else null
                )


                EventDescription(event)
            }

            Button(
                onClick = {

                    viewModel.createTicket(eventDetails.eventID)
                    navigateToPayment = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EventionBlue),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = "BUY TICKET ${event.price}€",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun Preview() {
    EventionTheme {
        val navController = rememberNavController()
        val event = MockData.events.first()
        EventDetails(MockData.events.first(), navController = navController)
    }
}
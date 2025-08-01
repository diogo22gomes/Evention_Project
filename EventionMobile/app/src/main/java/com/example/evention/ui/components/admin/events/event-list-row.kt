package com.example.evention.ui.components.admin.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.evention.model.Event
import com.example.evention.ui.theme.EventionBlue
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import UserPreferences
import android.net.Uri
import com.google.gson.Gson
import getUnsafeOkHttpClient
import com.example.evention.R

fun formatDate(date: Date): String {
    val zonedDateTime = date.toInstant()
        .atZone(ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("MMM dd · h:mma", Locale.ENGLISH)
    return zonedDateTime.format(formatter)
}

@Composable
fun EventListRow(
    event: Event,
    ticketID: String? = null,
    firstSection: String,
    secondSection: String,
    onEdit: (Event) -> Unit,
    onRemove: (Event) -> Unit,
    navController: NavController,
    thirdSection: String? = null
) {
    val showMenu = firstSection.isNotBlank() || secondSection.isNotBlank()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient {
                getUnsafeOkHttpClient(userPreferences)
            }
            .build()
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            val eventJson = Uri.encode(Gson().toJson(event))
            when (currentRoute) {
                "tickets" -> navController.navigate("ticketDetails/${ticketID}")
                "allEvents" -> navController.navigate("eventDetails/${eventJson}")
                "userEvents" -> navController.navigate("userParticipation/${eventJson}")
                else -> navController.navigate("eventDetails/${eventJson}")
            }
        }

    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = event.eventPicture?.let { "https://10.0.2.2:5010/event$it" }

            if (event.eventPicture == null) {
                Image(
                    painter = painterResource(id = R.drawable.default_event),
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = imageUrl,
                    imageLoader = imageLoader,
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = formatDate(event.startAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = EventionBlue
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = event.addressEvents.getOrNull(0)?.road.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (showMenu) {
                var expanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Mais opções")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .border(1.dp, EventionBlue, RoundedCornerShape(8.dp))
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    firstSection,
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            onClick = {
                                expanded = false
                                onEdit(event)
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .align(Alignment.CenterHorizontally)
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    secondSection,
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            onClick = {
                                expanded = false
                                onRemove(event)
                            }
                        )

                        if (thirdSection != null) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .align(Alignment.CenterHorizontally)
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        thirdSection,
                                        color = Color.Black,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                onClick = {
                                    expanded = false
                                    navController.navigate("scanQRCode")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

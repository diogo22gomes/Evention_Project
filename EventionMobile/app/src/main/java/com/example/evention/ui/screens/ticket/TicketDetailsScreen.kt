package com.example.evention.ui.screens.ticket

import TicketRepository
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.theme.EventionBlue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.evention.data.local.database.AppDatabase
import com.example.evention.data.local.factory.TicketDetailsViewModelFactory
import com.example.evention.di.NetworkModule
import com.example.evention.utils.isNetworkAvailable
import generateQrCodeBitmap
import java.time.ZoneId

@Composable
fun TicketDetailsScreen(ticketId: String, navController: NavController, fromPayment: Boolean = false) {
    val context = LocalContext.current
    val hasInternet = remember { mutableStateOf(isNetworkAvailable(context)) }
    val repository = remember {
        val db = AppDatabase.getDatabase(context)
        TicketRepository(
            remote = NetworkModule.ticketRemoteDataSource,
            eventRemote = NetworkModule.eventRemoteDataSource,
            local = db.ticketDao()
        )
    }

    val viewModel: TicketDetailsScreenViewModel = viewModel(
        factory = TicketDetailsViewModelFactory(repository)
    )

    val ticketNullable by viewModel.ticket.collectAsState()

    LaunchedEffect(ticketId) {
        viewModel.loadTicketById(ticketId)
    }

    ticketNullable?.let { ticket ->

        val locale = Locale("pt", "PT")
        val startDateTime =
            ticket.event.startAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val endDateTime =
            ticket.event.endAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val dateFormatter = DateTimeFormatter.ofPattern("d MMMM 'de' yyyy", locale)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val qrBitmap = remember(ticket.ticketID) {
            generateQrCodeBitmap(ticket.ticketID)
        }

        val eventEndAtLocalDateTime = ticket.event.endAt.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 28.dp)
        ) {

            TitleComponent("Ticket Details", true, navController, fromPayment)

            // Nome do evento
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = ticket.event.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Calendário + data e hora
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    modifier = Modifier.size(28.dp),
                    tint = EventionBlue
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = startDateTime.format(dateFormatter),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${startDateTime.format(timeFormatter)} - ${
                            endDateTime.format(
                                timeFormatter
                            )
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Localização
            if (ticket.event.addressEvents.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(28.dp),
                        tint = EventionBlue
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = ticket.event.addressEvents[0].road,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = ticket.event.addressEvents[0].postCode,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            // QR Code
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = qrBitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(300.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (LocalDateTime.now()
                    .isAfter(eventEndAtLocalDateTime) && ticket.feedback_id == null
            ) {
                Button(
                    onClick = {
                        if(hasInternet.value){
                            navController.navigate("ticketFeedback/${ticketId}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EventionBlue),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = "Give Feedback",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
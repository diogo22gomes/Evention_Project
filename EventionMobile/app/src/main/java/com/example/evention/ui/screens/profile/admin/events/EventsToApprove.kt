package com.example.evention.ui.screens.profile.admin.events

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.R
import com.example.evention.model.Event
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.components.admin.events.EventListRow
import com.example.evention.ui.theme.EventionTheme
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import com.example.evention.ui.theme.EventionBlue

@Composable
fun EventsToApprove(events: List<Event>, navController: NavController, viewModel: EventsToApproveViewModel = viewModel()) {
    val approveSuccess by viewModel.approveSuccess.collectAsState()
    val deleteSuccess by viewModel.deleteSuccess.collectAsState()

    var showApproveBanner by remember { mutableStateOf(false) }
    var showRemoveBanner by remember { mutableStateOf(false) }

    LaunchedEffect(approveSuccess) {
        if (approveSuccess) {
            showApproveBanner = true
            delay(2000)
            showApproveBanner = false
            viewModel.clearApproveSuccess()
        }
    }

    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess) {
            showRemoveBanner = true
            delay(2000)
            showRemoveBanner = false
            viewModel.clearDeleteSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = showApproveBanner,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .zIndex(1f)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = EventionBlue),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Approved",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Event approved successfully",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = showRemoveBanner,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
                .zIndex(1f)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Red),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Removed",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Event rejected successfully",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 18.dp)
        ) {

            TitleComponent("Approve Events", true, navController)

            if (events.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.noevents),
                            contentDescription = "No Events"
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No suspended events yet",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            } else {

                LazyColumn {
                    items(events.size) { index ->
                        EventListRow(
                            event = events[index],
                            firstSection = "Approve event",
                            secondSection = "Reject event",
                            onEdit = { viewModel.approveEvent(events[index].eventID) },
                            onRemove = { viewModel.deleteEvent(events[index].eventID) },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsToApprovePreview() {
    EventionTheme {
        val navController = rememberNavController()
        val events = listOf<Event>()
        EventsToApprove(events, navController)
    }
}
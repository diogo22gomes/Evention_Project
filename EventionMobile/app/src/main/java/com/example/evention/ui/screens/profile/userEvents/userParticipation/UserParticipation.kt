package com.example.evention.ui.screens.profile.userEvents.userParticipation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.mock.MockData
import com.example.evention.model.Event
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.components.profile.ParticipantCard
import com.example.evention.ui.theme.EventionTheme

@Composable
fun UserParticipation(
    event: Event,
    navController: NavController,
    viewModel: UserParticipationViewModel = viewModel()
) {
    LaunchedEffect(event.eventID) {
        viewModel.loadTickets(event.eventID)
    }

    val ticketWithUsers by viewModel.ticketsWithUsers.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 18.dp)
    ) {
        item {
            TitleComponent("Participants", true, navController)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (ticketWithUsers.isEmpty()) {
            item {
                Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No participations yet.", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            items(ticketWithUsers) { item ->
                ParticipantCard(user = item.user)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserParticipationPreview() {
    EventionTheme {
        val navController = rememberNavController()
        UserParticipation(MockData.events.first(), navController = navController)
    }
}
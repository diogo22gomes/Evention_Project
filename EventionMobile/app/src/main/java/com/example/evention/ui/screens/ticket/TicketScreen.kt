package com.example.evention.ui.screens.ticket

import androidx.compose.foundation.Image
import com.example.evention.mock.TicketMockData
import com.example.evention.model.Ticket
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evention.R
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.components.admin.events.EventListRow
import com.example.evention.ui.theme.EventionTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.ui.components.MenuComponent

@Composable
fun TicketsScreen(tickets: List<Ticket>, navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            MenuComponent(
                currentPage = "Tickets",
                navController = navController
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 18.dp)
                .padding(innerPadding)
        ) {

            TitleComponent("Tickets", false, navController)

            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 150.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.noevents),
                            contentDescription = "No tickets"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No tickets available",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                LazyColumn {
                    items(tickets.size) { index ->
                        EventListRow(
                            event = tickets[index].event,
                            ticketID = tickets[index].ticketID,
                            firstSection = "",
                            secondSection = "",
                            onEdit = {},
                            onRemove = {},
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
fun TicketsScreenPreview() {
    EventionTheme {
        val navController = rememberNavController()
        TicketsScreen(TicketMockData.tickets, navController = navController)
    }
}
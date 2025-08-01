package com.example.evention.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evention.mock.MockData
import com.example.evention.model.Event
import com.example.evention.ui.components.home.EventCard
import com.example.evention.ui.components.home.HomeSearch
import com.example.evention.ui.theme.EventionTheme
import com.example.evention.ui.components.MenuComponent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.R
import com.example.evention.ui.components.home.FilterButtonWithDateRange
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.utils.isNetworkAvailable

@Composable
fun HomeScreen(events: List<Event>, navController: NavController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

    val searchQuery = remember { mutableStateOf("") }

    val selectedStartDate = remember { mutableStateOf<Long?>(null) }
    val selectedEndDate = remember { mutableStateOf<Long?>(null) }

    val isFilterActive = selectedStartDate.value != null && selectedEndDate.value != null

    val filteredEvents = events.filter { event ->
        val matchesSearch = event.name.contains(searchQuery.value, ignoreCase = true)

        if (!isFilterActive) {
            matchesSearch
        } else {
            val eventStart = event.startAt.time
            val eventEnd = event.endAt.time

            val inDateRange =
                eventStart >= selectedStartDate.value!! && eventEnd <= selectedEndDate.value!!

            matchesSearch && inDateRange
        }
    }

    LaunchedEffect(Unit) {
        isConnected = isNetworkAvailable(context)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            MenuComponent(
                currentPage = "Home",
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 30.dp),
        ) {
            item {
                HomeSearch(
                    searchQuery = searchQuery.value,
                    onSearchChange = { searchQuery.value = it },
                    navController = navController
                )
            }

            item {
                Spacer(modifier = Modifier.height(15.dp))
            }

            if(isConnected){
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                    ) {
                        Text(
                            text = "Upcoming Events",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isFilterActive) {
                                TextButton(
                                    onClick = {
                                        selectedStartDate.value = null
                                        selectedEndDate.value = null
                                        if(searchQuery.value != "") searchQuery.value = ""
                                    }
                                ) {
                                    Text("Clear", color = Color.Gray)
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            FilterButtonWithDateRange(
                                onDateRangeSelected = { start, end ->
                                    selectedStartDate.value = start
                                    selectedEndDate.value = end
                                }
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }

                if (filteredEvents.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 50.dp),
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
                                    text = "No events found",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                } else {
                    items(filteredEvents) { event ->
                        EventCard(
                            event = event,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp, vertical = 8.dp),
                            navController = navController
                        )
                    }
                }
            } else {
                item {
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
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    EventionTheme {
        val navController = rememberNavController()
        HomeScreen(events = MockData.events, navController = navController)
    }
}
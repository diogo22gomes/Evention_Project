package com.example.evention.ui.screens.profile.user.userProfile

import UserPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evention.model.User
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.components.profile.MenuCard
import com.example.evention.ui.components.profile.UserInfo
import com.example.evention.ui.theme.EventionTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.ui.components.MenuComponent
import com.example.evention.ui.components.profile.FeedbackRow
import com.example.evention.ui.components.profile.RatingStars
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.utils.isNetworkAvailable

@Composable
fun UserProfile(
    navController: NavController,
    userProfile: User? = null,
    viewModel: UserProfileViewModel = viewModel()
) {
    val userNullable by viewModel.user.collectAsState()
    val reputation by viewModel.reputation.collectAsState()
    val eventsMap by viewModel.events.collectAsState()
    val context = LocalContext.current

    val userPrefs = remember { UserPreferences(context) }
    val usertype = userPrefs.getUserType()

    val isAdmin = usertype == "123e4567-e89b-12d3-a456-426614174002"
    val isAdvertiser = usertype == "123e4567-e89b-12d3-a456-426614174001"

    val user = userProfile ?: userNullable

    var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

    LaunchedEffect(navController) {
        if (userProfile == null && isConnected) {
            viewModel.loadUserProfile()
        } else if (userProfile != null) {
            viewModel.loadUserReputation(userProfile.userID)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        bottomBar = {
            if (userProfile == null) {
                MenuComponent(
                    currentPage = "Profile",
                    navController = navController
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 18.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!isConnected) {
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
                                if (isConnected && userProfile == null) {
                                    viewModel.loadUserProfile()
                                }
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
            } else {
                user?.let {
                    TitleComponent("Profile", userProfile != null, navController)

                    UserInfo(it, navController, userProfile != null)

                    reputation?.let { rep ->
                        Spacer(modifier = Modifier.height(12.dp))

                        RatingStars(rating = rep.reputation)

                        val feedbackEventCount = rep.tickets
                            .filter { it.feedback != null }
                            .map { it.event_id }
                            .distinct()
                            .count()

                        Text(
                            text = "($feedbackEventCount)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    if (userProfile == null) {
                        MenuCard(navController, isAdmin, isAdvertiser)
                    } else {
                        Text(
                            text = "Feedbacks Received",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 16.dp)
                        )

                        LaunchedEffect(reputation) {
                            reputation?.tickets
                                ?.map { it.event_id }
                                ?.distinct()
                                ?.forEach { viewModel.loadEventById(it) }
                        }

                        val ticketsWithFeedback =
                            reputation?.tickets?.filter { it.feedback != null }.orEmpty()

                        if (ticketsWithFeedback.isEmpty()) {
                            Text(
                                "No feedbacks received.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            ticketsWithFeedback.forEach { ticket ->
                                val eventName = eventsMap[ticket.event_id]?.name ?: "Loading..."
                                FeedbackRow(
                                    feedback = ticket.feedback!!,
                                    eventName = eventName
                                )
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
        UserProfile(navController = navController)
    }
}
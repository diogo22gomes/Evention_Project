package com.example.evention.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MenuCard(navController: NavController, isAdmin: Boolean? = null, isAdvertiser: Boolean? = null){
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            if(isAdmin == true){
                ProfileMenuItem(icon = Icons.Outlined.Person, label = "Admin Menu", sublabel = "Manage users and events", navController = navController)
            }
            if(isAdvertiser == true || isAdmin == true){
                ProfileMenuItem(icon = Icons.Outlined.DateRange, label = "My Events", sublabel = "Manage and explore your events", navController = navController)
            }
            ProfileMenuItem(
                icon = Icons.Outlined.Notifications,
                label = "Notifications",
                isNotification = true, sublabel = "Turn on/off your notifications",
                navController = navController
            )
            ProfileMenuItem(icon = Icons.Outlined.Build, label = "Change Password", sublabel = "Change your password", navController = navController)
            ProfileMenuItem(icon = Icons.Outlined.Info, label = "Logout", sublabel = "Log out of your account", navController = navController)
        }
    }
}
package com.example.evention.ui.components.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evention.ui.components.profile.ProfileMenuItem

@Composable
fun MenuItem(navController: NavController){
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
            ProfileMenuItem(icon = Icons.Outlined.Person, label = "All Users", sublabel = "View and Manage all registered users", navController = navController)
            ProfileMenuItem(icon = Icons.Outlined.DateRange, label = "All Events", sublabel = "Manage all events", navController = navController)
            ProfileMenuItem(icon = Icons.Outlined.Info, label = "Events to approve", sublabel = "Review and approve pending events", navController = navController)
        }
    }
}
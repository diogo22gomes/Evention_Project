package com.example.evention.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.annotation.DrawableRes
import androidx.navigation.NavController
import com.example.evention.R
import com.example.evention.ui.theme.EventionBlue

@Composable
fun MenuComponent(
    currentPage: String,
    navController: NavController
) {
    val menuItems = listOf(
        Triple("Home", R.drawable.home, R.drawable.home_filled),
        Triple("Search", R.drawable.search, R.drawable.search_filled),
        Triple("Create", R.drawable.add, R.drawable.add_filled),
        Triple("Tickets", R.drawable.ticket, R.drawable.ticket_filled),
        Triple("Profile", R.drawable.profile_circle, R.drawable.profile_filled)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        menuItems.forEach { (name, icon, iconFilled) ->
            MenuItem(
                name = name,
                iconRes = if (currentPage == name) iconFilled else icon,
                currentPage = currentPage,
                onClick = {
                    val route = when (name) {
                        "Home" -> "home"
                        "Search" -> "search"
                        "Create" -> "create"
                        "Tickets" -> "tickets"
                        "Profile" -> "profile"
                        else -> "home"
                    }
                    navController.navigate(route)
                }
            )
        }
    }
}


@Composable
fun MenuItem(
    name: String,
    @DrawableRes iconRes: Int,
    currentPage: String,
    onClick: (String) -> Unit
) {
    val isSelected = name == currentPage
    val textColor = if (isSelected) EventionBlue else Color(0xFF3E3E3E)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick(name) }
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = name,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            fontSize = 12.sp
        )
    }
}

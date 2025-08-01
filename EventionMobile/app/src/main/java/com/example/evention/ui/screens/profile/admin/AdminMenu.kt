package com.example.evention.ui.screens.profile.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.components.admin.MenuItem
import com.example.evention.ui.theme.EventionTheme

@Composable
fun AdminMenu(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 18.dp)
    ) {

        TitleComponent("Admin Menu", true, navController = navController)

        MenuItem(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun AdminMenuPreview() {
    EventionTheme {
        val navController = rememberNavController()
        AdminMenu(navController)
    }
}
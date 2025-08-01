package com.example.evention.ui.components.profile

import UserPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.evention.ui.theme.EventionBlue

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    sublabel: String? = null,
    isNotification: Boolean = false,
    navController: NavController,
) {
    var switchState by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isNotification) {
                when (label) {
                    "Admin Menu" -> navController.navigate("adminMenu")
                    "My Events" -> navController.navigate("userEvents")
                    "Change Password" -> navController.navigate("changePassword")
                    "All Users" -> navController.navigate("allUsers")
                    "All Events" -> navController.navigate("allEvents")
                    "Events to approve" -> navController.navigate("approveEvents")
                    "Logout" -> {
                        userPrefs.clearToken()
                        userPrefs.clearUserId()
                        navController.navigate("signIn") {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                }
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .background(Color(0xFFEEF0FF), RoundedCornerShape(20.dp))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = EventionBlue,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            sublabel?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFABABAB)
                )
            }
        }

        if (isNotification) {
            Switch(
                checked = switchState,
                onCheckedChange = { switchState = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = EventionBlue,
                    checkedTrackColor = Color(0xFFE8E8E8)
                )
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = "Next",
                tint = Color.Gray
            )
        }
    }
}

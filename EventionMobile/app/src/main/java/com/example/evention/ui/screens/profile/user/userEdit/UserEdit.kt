package com.example.evention.ui.screens.profile.user.userEdit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.mock.MockUserData
import com.example.evention.model.User
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.components.userEdit.LabeledTextField
import com.example.evention.ui.components.userEdit.UserEditInfo
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.zIndex

@Composable
fun UserEdit(
    userToEdit: User,
    navController: NavController,
    viewModel: UserEditViewModel = viewModel()
) {
    val editSuccess by viewModel.editSuccess.collectAsState()
    var showBanner by remember { mutableStateOf(false) }

    userToEdit.let { user ->
        var username by remember { mutableStateOf(user.username) }
        var email by remember { mutableStateOf(user.email) }
        var phone by remember { mutableStateOf(user.phone?.toString() ?: "") }
        val context = LocalContext.current

        LaunchedEffect(editSuccess) {
            if (editSuccess) {
                showBanner = true
                delay(2000)
                showBanner = false
                viewModel.clearEditSuccess()
                navController.popBackStack()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {

            AnimatedVisibility(
                visible = showBanner,
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
                            contentDescription = "Success",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "User udpated successfully",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                TitleComponent("Edit Profile", arrowBack = true, navController = navController)

                Spacer(modifier = Modifier.size(16.dp))

                UserEditInfo(user, viewModel)

                Spacer(modifier = Modifier.size(24.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    LabeledTextField(
                        label = "Username",
                        value = username,
                        onValueChange = { username = it }
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    LabeledTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it }
                    )

                    Spacer(modifier = Modifier.size(8.dp))

                    LabeledTextField(
                        label = "Phone",
                        value = phone,
                        onValueChange = { phone = it }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val phoneInt = phone.toIntOrNull() ?: 0
                        viewModel.editUser(
                            context = context,
                            userId = user.userID,
                            username = username,
                            email = email,
                            phone = phoneInt
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EventionBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Save Changes",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserEditPreview() {
    EventionTheme {
        val navController = rememberNavController()
        UserEdit(MockUserData.users.first(), navController = navController)
    }
}
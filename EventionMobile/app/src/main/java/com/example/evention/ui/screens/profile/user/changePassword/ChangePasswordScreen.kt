package com.example.evention.ui.screens.profile.user.changePassword

import AuthConfirmButton
import AuthTextField
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.evention.R
import com.example.evention.ui.theme.EventionBlue
import kotlinx.coroutines.delay

@Composable
fun ChangePasswordScreen(
    navController: NavController,
    viewModel: ChangePasswordViewModel = viewModel()
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }

    val changePasswordState by viewModel.state.collectAsState()

    val showMessage = remember { mutableStateOf(false) }
    val messageText = remember { mutableStateOf("") }
    val isSuccessMessage = remember { mutableStateOf(true) }

    val buttonState = when (changePasswordState) {
        is ChangePasswordViewModel.ChangePasswordState.Loading -> ButtonState.LOADING
        is ChangePasswordViewModel.ChangePasswordState.Success -> ButtonState.SUCCESS
        is ChangePasswordViewModel.ChangePasswordState.Error -> ButtonState.ERROR
        else -> ButtonState.IDLE
    }

    // message laucnher
    LaunchedEffect(changePasswordState) {
        when (val state = changePasswordState) {
            is ChangePasswordViewModel.ChangePasswordState.Success -> {
                messageText.value = "Password changed successfully!"
                isSuccessMessage.value = true
                showMessage.value = true

                delay(1800)
                showMessage.value = false
                viewModel.resetState()

                navController.navigate("profile") {
                    popUpTo("changePassword") { inclusive = true }
                }
            }

            is ChangePasswordViewModel.ChangePasswordState.Error -> {
                messageText.value = state.message
                isSuccessMessage.value = false
                showMessage.value = true

                delay(1800)
                showMessage.value = false
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Arrow Back",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .size(24.dp)
                        .clickable { navController.navigate("profile") },
                    tint = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Change Password",
                    fontSize = 24.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Please enter your old password and your\nnew password and confirm it",
                    fontSize = 15.sp,
                    color = Color(0xFF120D26),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(30.dp))

                AuthTextField(
                    placeholderText = "Old Password",
                    iconResId = R.drawable.lock,
                    value = oldPassword,
                    password = true,
                    onValueChange = { oldPassword = it }
                )

                Spacer(modifier = Modifier.height(22.dp))

                AuthTextField(
                    placeholderText = "Your Password",
                    iconResId = R.drawable.lock,
                    value = newPassword,
                    password = true,
                    onValueChange = { newPassword = it }
                )

                Spacer(modifier = Modifier.height(22.dp))

                AuthTextField(
                    placeholderText = "Confirm Password",
                    iconResId = R.drawable.lock,
                    value = confirmNewPassword,
                    password = true,
                    onValueChange = { confirmNewPassword = it }
                )

                Spacer(modifier = Modifier.height(50.dp))

                AuthConfirmButton(
                    text = "Confirm",
                    state = buttonState,
                    onClick = {
                        viewModel.resetState()
                        viewModel.changePassword(oldPassword, newPassword, confirmNewPassword)
                    }
                )
            }

            // floating card
            AnimatedVisibility(
                visible = showMessage.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .zIndex(2f)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSuccessMessage.value) EventionBlue else Color(0xFFD32F2F)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = if (isSuccessMessage.value) Icons.Default.CheckCircle else Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = messageText.value,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}

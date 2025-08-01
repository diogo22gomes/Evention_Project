package com.example.evention.ui.screens.auth.resetpassword

import AuthConfirmButton
import AuthTextField
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.R
import com.example.evention.data.remote.authentication.ResetPasswordViewModelFactory
import com.example.evention.ui.theme.EventionTheme
import kotlinx.coroutines.delay

@Composable
fun ResetPasswordScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: ResetPasswordViewModel =
        viewModel(factory = ResetPasswordViewModelFactory(context))

    var email by remember { mutableStateOf("") }

    val state by viewModel.state.collectAsState()

    val showMessage = remember { mutableStateOf(false) }
    val messageText = remember { mutableStateOf("") }
    val isSuccessMessage = remember { mutableStateOf(true) }

    val buttonState = when (state) {
        is ResetPasswordViewModel.ResetPasswordState.Loading -> ButtonState.LOADING
        is ResetPasswordViewModel.ResetPasswordState.Success -> ButtonState.SUCCESS
        is ResetPasswordViewModel.ResetPasswordState.Error -> ButtonState.ERROR
        else -> ButtonState.IDLE
    }

    LaunchedEffect(state) {
        when (state) {
            is ResetPasswordViewModel.ResetPasswordState.Success -> {
                messageText.value = "Email Sent successfully!"
                isSuccessMessage.value = true
                showMessage.value = true

                delay(2000)
                navController.navigate("confirmPassword") {
                    popUpTo("resetPassword") { inclusive = true }
                }
            }

            is ResetPasswordViewModel.ResetPasswordState.Error -> {
                messageText.value = "Incorrect Email!"
                isSuccessMessage.value = false
                showMessage.value = true

                delay(2000)
                viewModel.resetState()
            }

            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Floating card
        androidx.compose.animation.AnimatedVisibility(
            visible = showMessage.value,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .zIndex(2f)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSuccessMessage.value) Color(0xFF66BB6A) else Color(
                            0xFFD32F2F
                        )
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Arrow Back",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 16.dp)
                    .size(24.dp)
                    .clickable { navController.navigate("signIn") },
                tint = Color.Black
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Reset Password",
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Please enter your email address to\nrequest a password reset",
                fontSize = 15.sp,
                color = Color(0xFF120D26),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            AuthTextField(
                placeholderText = "abc@email.com",
                iconResId = R.drawable.mail,
                value = email,
                password = false,
                onValueChange = {
                    viewModel.resetState()
                    email = it
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            AuthConfirmButton(
                text = "SEND",
                state = buttonState,
                onClick = {
                    viewModel.resetState()
                    viewModel.resetPassword(email)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    EventionTheme {
        val navController = rememberNavController()
        ResetPasswordScreen(navController = navController)
    }
}
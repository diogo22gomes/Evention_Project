package com.example.evention.ui.screens.auth.register

import AuthTextField
import AuthConfirmButton
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.R
import com.example.evention.data.remote.authentication.RegisterViewModelFactory
import com.example.evention.ui.components.auth.AuthGoogle
import com.example.evention.ui.theme.EventionTheme
import kotlinx.coroutines.delay
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.zIndex
import com.example.evention.ui.screens.auth.login.signInWithGoogle
import com.example.evention.ui.screens.event.create.CreateEventState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: RegisterScreenViewModel = viewModel(factory = RegisterViewModelFactory(context))

    //GOOGLE AUTH
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                viewModel.registerWithGoogle(idToken)
            } else {
                Log.e("GOOGLE", "Token inválido")
            }
        } catch (e: ApiException) {
            Log.e("GOOGLE", "Erro Google Sign-In", e)
        }
    }

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }

    val showMessage = remember { mutableStateOf(false) }
    val messageText = remember { mutableStateOf("") }
    val isSuccessMessage = remember { mutableStateOf(true) }

    val registerState by viewModel.registerState.collectAsState()

    val buttonState = when (registerState) {
        is RegisterScreenViewModel.RegisterState.Loading -> ButtonState.LOADING
        is RegisterScreenViewModel.RegisterState.Success -> ButtonState.SUCCESS
        is RegisterScreenViewModel.RegisterState.Error -> ButtonState.ERROR
        else -> ButtonState.IDLE
    }

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is RegisterScreenViewModel.RegisterState.Success -> {
                messageText.value = "User registed successfully!"
                isSuccessMessage.value = true
                showMessage.value = true

                delay(2000)
                showMessage.value = false

                navController.navigate("signIn") {
                    popUpTo("signUp") { inclusive = true }
                }
            }

            is RegisterScreenViewModel.RegisterState.Error -> {
                messageText.value = state.message
                isSuccessMessage.value = false
                showMessage.value = true

                delay(2000)
                viewModel.resetState()
                showMessage.value = false
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
                text = "Sign up",
                fontSize = 24.sp,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(22.dp))

            AuthTextField(
                placeholderText = "Full Name",
                iconResId = R.drawable.profile,
                value = username,
                password = false,
                onValueChange = { username = it }
            )

            Spacer(modifier = Modifier.height(22.dp))

            AuthTextField(
                placeholderText = "abc@email.com",
                iconResId = R.drawable.mail,
                value = email,
                password = false,
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(22.dp))

            AuthTextField(
                placeholderText = "Your Password",
                iconResId = R.drawable.lock,
                value = password,
                password = true,
                onValueChange = { password = it }
            )

            Spacer(modifier = Modifier.height(22.dp))

            AuthTextField(
                placeholderText = "Confirm Password",
                iconResId = R.drawable.lock,
                value = confirmpassword,
                password = true,
                onValueChange = { confirmpassword = it }
            )

            Spacer(modifier = Modifier.height(50.dp))

            AuthConfirmButton(
                text = "Sign up",
                state = buttonState,
                onClick = {
                    viewModel.resetState()
                    viewModel.register(username, email, password, confirmpassword)
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                "OR",
                color = Color(0xFF9D9898),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(30.dp))

            AuthGoogle(
                text = "Registar com Google",
                onClick = {
                    signInWithGoogle(context, launcher)
                }
            )


            Spacer(modifier = Modifier.height(120.dp))

            Row {
                Text(
                    text = "Already have an account?",
                    color = Color(0xFF120D26),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp
                )
                Text(
                    text = " Sign in",
                    modifier = Modifier.clickable { navController.navigate("signIn") },
                    color = Color(0xFF5669FF),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    EventionTheme {
        val navController = rememberNavController()
        RegisterScreen(navController = navController)
    }
}
package com.example.evention.ui.screens.auth.login
import AuthConfirmButton
import AuthTextField
import UserPreferences
import androidx.activity.result.ActivityResult
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.R
import com.example.evention.notifications.RequestNotificationPermission
import com.example.evention.data.remote.authentication.LoginViewModelFactory
import com.example.evention.ui.components.auth.AuthGoogle
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.delay


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LoginScreenViewModel = viewModel(factory = LoginViewModelFactory(context))
    val userPreferences = remember(context) { UserPreferences(context) }
    //GOOGLE AUTH
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                Log.d("Token", idToken)
                viewModel.loginWithGoogle(idToken)
            } else {
                Log.e("GOOGLE", "Token inválido")
            }
        } catch (e: ApiException) {
            Log.e("GOOGLE", "Erro Google Sign-In", e)
        }
    }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMeChecked by remember { mutableStateOf(true) }
    var showNotificationPermission by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    val loginState = viewModel.loginState

    val buttonState = when (loginState) {
        is LoginScreenViewModel.LoginState.Loading -> ButtonState.LOADING
        is LoginScreenViewModel.LoginState.Success -> ButtonState.SUCCESS
        is LoginScreenViewModel.LoginState.Error -> ButtonState.ERROR
        else -> ButtonState.IDLE
    }

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginScreenViewModel.LoginState.Success -> {
                if (!userPreferences.isNotificationPermissionShown()) {
                    showNotificationPermission = true
                }
                errorMessage = null
                delay(500)
                viewModel.resetState()
                navController.navigate("home") {
                    popUpTo("signIn") { inclusive = true }
                }
            }

            is LoginScreenViewModel.LoginState.Error -> {
                errorMessage = "Incorrect Email or Password!"
                delay(2000)
                viewModel.resetState()
            }

            else -> {}
        }
    }

    if (showNotificationPermission) {
        RequestNotificationPermission()
        userPreferences.setNotificationPermissionShown()
        showNotificationPermission = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logosimple),
            contentDescription = "Logo App Login",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Sign in",
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        AuthTextField(
            placeholderText = "abc@email.com",
            iconResId = R.drawable.mail,
            value = email,
            password = false,
            onValueChange = {
                email = it
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        AuthTextField(
            placeholderText = "Your Password",
            iconResId = R.drawable.lock,
            value = password,
            password = true,
            onValueChange = {
                password = it
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage ?: "",
                    color = Color.Red,
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = rememberMeChecked,
                onCheckedChange = { rememberMeChecked = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.White,
                    checkedTrackColor = EventionBlue,
                    uncheckedTrackColor = Color.LightGray
                ),
                modifier = Modifier.scale(0.75f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Remember Me",
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF120D26)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF120D26),
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("resetPassword") }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        AuthConfirmButton(
            text = "Sign in",
            state = buttonState,
            onClick = {
                viewModel.resetState()
                viewModel.login(email, password)
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
            text = "Login com Google",
            onClick = {
                signInWithGoogle(context, launcher)
            }
        )

        Spacer(modifier = Modifier.height(80.dp))

        Row {
            Text(
                text = "Don’t have an account?",
                color = Color(0xFF120D26),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.sp
            )
            Text(
                text = " Sign up",
                modifier = Modifier.clickable { navController.navigate("signUp") },
                color = EventionBlue,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 15.sp
            )
        }
    }
}

fun signInWithGoogle(
    context: Context,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)
    val signInIntent = googleSignInClient.signInIntent
    launcher.launch(signInIntent)
}



@Preview(showBackground = true)
@Composable
fun Preview() {
    EventionTheme {
        val navController = rememberNavController()
        LoginScreen(navController = navController)
    }
}
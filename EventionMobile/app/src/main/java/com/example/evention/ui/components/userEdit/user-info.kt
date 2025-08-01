package com.example.evention.ui.components.userEdit

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.evention.model.User
import com.example.evention.ui.theme.EventionBlue
import UserPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.layout.ContentScale
import coil.ImageLoader
import com.example.evention.ui.screens.profile.user.userEdit.UserEditViewModel
import getUnsafeOkHttpClient
import com.example.evention.R

@Composable
fun UserEditInfo(user: User, viewModel: UserEditViewModel) {
    val context = LocalContext.current
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val userPreferences = remember { UserPreferences(context) }

    val imageLoader = remember {
        ImageLoader.Builder(context)
            .okHttpClient {
                getUnsafeOkHttpClient(userPreferences)
            }
            .build()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setSelectedImageUri(it) }
    }

    val hasNewImage = selectedImageUri != null
    val imageModel: Any? = when {
        hasNewImage -> selectedImageUri
        user.profilePicture != null -> "https://10.0.2.2:5010/user${user.profilePicture}"
        else -> null
    }

    Box(
        modifier = Modifier
            .size(170.dp)
            .clip(CircleShape)
            .background(Color.Gray.takeIf { imageModel == null } ?: Color.Transparent)
            .clickable {
                launcher.launch("image/*")
            }
    ) {
        if (imageModel != null) {
            AsyncImage(
                model = imageModel,
                imageLoader = imageLoader,
                contentDescription = "User Profile Image",
                onError = {
                    Log.e("UserEditInfo", "Erro ao carregar imagem: $it")
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.default_user),
                contentDescription = "User Profile Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        onClick = {
            launcher.launch("image/*")
        },
        modifier = Modifier
            .height(36.dp)
            .padding(horizontal = 8.dp)
            .border(1.dp, EventionBlue, RoundedCornerShape(20.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(20.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(
            text = "Change Avatar",
            color = EventionBlue,
            fontWeight = FontWeight.Medium
        )
    }
}


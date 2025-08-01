package com.example.evention.ui.screens.profile.user

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ScanQRCodeScreen(navController: NavController) {
    var scannedCode by remember { mutableStateOf<String?>(null) }
    var boundingBoxes by remember { mutableStateOf(emptyList<android.graphics.Rect>()) }
    val context = LocalContext.current

    var cameraPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionGranted = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Permissão de câmera é necessária", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionGranted) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 28.dp)
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ticket Details",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(28.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        // QR Code
        Box(
            modifier = Modifier
                .weight(5f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            if (cameraPermissionGranted) {
                QRCodeScannerView(
                    modifier = Modifier.fillMaxSize(),
                    onQRCodeScanned = { code -> scannedCode = code },
                    onBoundingBoxesDetected = { rects -> boundingBoxes = rects }
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val scaleX = size.width / 480f
                    val scaleY = size.height / 640f

                    boundingBoxes.forEach { rect ->
                        drawRect(
                            color = EventionBlue,
                            topLeft = Offset(rect.left * scaleX, rect.top * scaleY),
                            size = androidx.compose.ui.geometry.Size(
                                rect.width() * scaleX,
                                rect.height() * scaleY
                            ),
                            style = Stroke(width = 4f)
                        )
                    }
                }
            } else {
                Text(
                    text = "Câmera não tem permissão!",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .background(Color(0x80000000), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }

            scannedCode?.let { code ->
                Text(
                    text = "Scanned: $code",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .background(Color(0x80000000), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun TicketDetailsPreview() {
    EventionTheme {
        ScanQRCodeScreen(navController = rememberNavController())
    }
}

package com.example.evention.ui.screens.profile.admin.editEvent

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.evention.R
import com.example.evention.mock.MockData
import com.example.evention.model.Event
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.components.userEdit.LabeledTextField
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme
import java.text.SimpleDateFormat
import java.util.*
import UserPreferences
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import getUnsafeOkHttpClient
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.zIndex

@Composable
fun EditEvent(
    eventToEdit: Event,
    navController: NavController,
    viewModel: EditEventViewModel = viewModel()
) {
    eventToEdit.let { event ->
        var name by remember { mutableStateOf(event.name) }
        var description by remember { mutableStateOf(event.description) }
        var price by remember { mutableStateOf(event.price.toString()) }

        val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
        val selectedImageUri by viewModel.selectedImageUri.collectAsState()
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { viewModel.setSelectedImageUri(it) }
        }

        val hasNewImage = selectedImageUri != null
        val imageModel: Any? = when {
            hasNewImage -> selectedImageUri
            event.eventPicture != null -> "https://10.0.2.2:5010/event${event.eventPicture}"
            else -> null
        }

        val isoFormatter = remember {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
        }

        var showDatePicker by remember { mutableStateOf(false) }
        var startDateMillis by remember { mutableStateOf(event.startAt.time) }
        var endDateMillis by remember { mutableStateOf(event.endAt.time) }

        val editSuccess by viewModel.editSuccess.collectAsState()
        var showBanner by remember { mutableStateOf(false) }

        LaunchedEffect(editSuccess) {
            if (editSuccess) {
                showBanner = true
                delay(2000)
                showBanner = false
                viewModel.clearEditSuccess()
                navController.popBackStack()
            }
        }

        val userPreferences = remember { UserPreferences(context) }
        val imageLoader = remember {
            ImageLoader.Builder(context)
                .okHttpClient {
                    getUnsafeOkHttpClient(userPreferences)
                }
                .build()
        }

        if (showDatePicker) {
            DateRangePickerModal(
                initialStartDate = startDateMillis,
                initialEndDate = endDateMillis,
                onDateRangeSelected = { start, end ->
                    if (start != null && end != null) {
                        startDateMillis = start
                        endDateMillis = end
                    }
                },
                onDismiss = { showDatePicker = false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 18.dp)
        ) {
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
                            text = "Event updated successfully",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    TitleComponent("Event Editor", arrowBack = true, navController = navController)

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true }
                    ) {
                        OutlinedTextField(
                            value = "${formatter.format(Date(startDateMillis))} - ${formatter.format(Date(endDateMillis))}",
                            onValueChange = {},
                            textStyle = TextStyle(color = Color.Black),
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.calendar_picker),
                                    contentDescription = "Calendar logo",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        if (imageModel != null) {
                            AsyncImage(
                                model = imageModel,
                                imageLoader = imageLoader,
                                contentDescription = "Event Image",
                                onError = {
                                    Log.e("EventEditInfo", "Erro ao carregar imagem: $it")
                                },
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Button(
                            onClick = { launcher.launch("image/*") },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(width = 100.dp, height = 34.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.4f)),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.blue_camera),
                                contentDescription = "Blue camera icon",
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "CHANGE",
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.bodyMedium,
                                color = EventionBlue,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(20.dp))

                    LabeledTextField(
                        label = "Event Name",
                        value = name,
                        onValueChange = { name = it }
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    LabeledTextField(
                        label = "Description",
                        value = description,
                        onValueChange = { description = it }
                    )

                    Spacer(modifier = Modifier.size(10.dp))

                    LabeledTextField(
                        label = "Price (€)",
                        value = price,
                        onValueChange = {
                            price = it.filter { ch -> ch.isDigit() || ch == '.' }
                        }
                    )

                }

                Button(
                    onClick = {
                        val isoStart = isoFormatter.format(Date(startDateMillis))
                        val isoEnd = isoFormatter.format(Date(endDateMillis))
                        val cleanedPrice = price.replace("[^\\d.]".toRegex(), "").toFloatOrNull() ?: 0f

                        viewModel.editEvent(
                            context = context,
                            eventId = event.eventID,
                            name = name,
                            description = description,
                            startAt = isoStart,
                            endAt = isoEnd,
                            price = cleanedPrice
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EventionBlue
                    )
                ) {
                    Text(
                        text = "Save Changes",
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    initialStartDate: Long,
    initialEndDate: Long,
    onDateRangeSelected: (startDate: Long?, endDate: Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartDate,
        initialSelectedEndDateMillis = initialEndDate
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(state.selectedStartDateMillis, state.selectedEndDateMillis)
                    onDismiss()
                }
            ) {
                Text("Confirm", color = EventionBlue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        },
        colors = DatePickerDefaults.colors(
            titleContentColor = EventionBlue,
            headlineContentColor = EventionBlue,
            weekdayContentColor = EventionBlue,
            selectedDayContainerColor = EventionBlue,
            selectedDayContentColor = Color.White
        )
    ) {
        DateRangePicker(
            state = state,
            title = {
                Text(text = "Select Event Date Range")
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserEditPreview() {
    EventionTheme {
        val navController = rememberNavController()
        EditEvent(MockData.events.first(), navController)
    }
}
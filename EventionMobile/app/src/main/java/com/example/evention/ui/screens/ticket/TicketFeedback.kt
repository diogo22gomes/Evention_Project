package com.example.evention.ui.screens.ticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.mock.TicketMockData
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme

@Composable
fun TicketFeedbackScreen(ticketId: String, navController: NavController) {

    val viewModel: FeedbackViewModel = viewModel()
    val feedbackResult by viewModel.createFeedbackResult.collectAsState()

    var rating by remember { mutableStateOf(0) }
    var commentary by remember { mutableStateOf("") }

    LaunchedEffect(feedbackResult) {
        feedbackResult?.let {
            it.onSuccess { feedback ->
                println("Feedback enviado: ${feedback.feedbackID}")
                navController.popBackStack()
            }.onFailure { error ->
                println("Erro ao enviar feedback: ${error.message}")
            }
            viewModel.clearFeedbackResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 28.dp)
    ) {

        TitleComponent("Event Feedback", true, navController)

        Text(
            text = "The Event Is Finished",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "How would you rate the event?",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )


        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(5) { index ->
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Star $index",
                    tint = if (index < rating) EventionBlue else Color.Gray,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            rating = index + 1
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Anything else?",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        OutlinedTextField(
            value = commentary,
            onValueChange = { commentary = it },
            label = { Text("Tell us everything.") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            shape = RoundedCornerShape(8.dp)
        )

        Button(
            onClick = {
                viewModel.createFeedback(ticketId, rating, commentary)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EventionBlue),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = "Submit",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketFeedbackPreview() {
    EventionTheme {
        TicketFeedbackScreen(TicketMockData.tickets[0].ticketID, navController = rememberNavController())
    }
}

package com.example.evention.ui.screens.home.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.evention.R
import com.example.evention.mock.MockData
import com.example.evention.model.Event
import com.example.evention.ui.components.TitleComponent
import com.example.evention.ui.theme.EventionBlue
import com.example.evention.ui.theme.EventionTheme

@Composable
fun PaymentScreen(event: Event,ticketId: String, navController: NavController, viewModel: PaymentViewModel) {

    val paymentResult by viewModel.paymentResult.collectAsState()
    var paymentTriggered by remember { mutableStateOf(false) }

    LaunchedEffect(paymentResult, paymentTriggered) {
        if (paymentTriggered && paymentResult != null) {
            navController.navigate("ticketDetails/$ticketId?fromPayment=true")
            paymentTriggered = false
        }
    }

    if (event == null) return

    var selectedMethod by remember { mutableStateOf("Paypal") }
    var paypalEmail by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

    val paymentOptions = listOf(
        "Paypal" to R.drawable.paypal,
        "Credit Card" to R.drawable.mastercard
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 25.dp, vertical = 18.dp)
    ) {
        TitleComponent("Checkout", false, navController)

        Text(
            text = "Payment",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp),
            fontWeight = FontWeight.Bold
        )

        Column {
            paymentOptions.forEach { (method, logoResId) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMethod = method }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = selectedMethod == method,
                        onClick = { selectedMethod = method }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = logoResId),
                        contentDescription = "$method logo",
                        modifier = Modifier
                            .size(36.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = method,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            when (selectedMethod) {
                "Paypal" -> {
                    OutlinedTextField(
                        value = paypalEmail,
                        onValueChange = { paypalEmail = it },
                        label = { Text("PayPal Email") },
                        placeholder = { Text("example@paypal.com") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                "Credit Card" -> {
                    OutlinedTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = { Text("Card Number") },
                        placeholder = { Text("1234 5678 9012 3456") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = cardExpiry,
                            onValueChange = { cardExpiry = it },
                            label = { Text("Expiry") },
                            placeholder = { Text("MM/YY") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = cardCvv,
                            onValueChange = { cardCvv = it },
                            label = { Text("CVV") },
                            placeholder = { Text("***") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${event.price} €",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val isPaypalValid = selectedMethod == "Paypal" && android.util.Patterns.EMAIL_ADDRESS.matcher(paypalEmail).matches()
                val isCardValid = selectedMethod == "Credit Card" &&
                        cardNumber.isNotBlank() &&
                        cardExpiry.isNotBlank() &&
                        cardCvv.isNotBlank()

                if ((selectedMethod == "Paypal" && isPaypalValid) || (selectedMethod == "Credit Card" && isCardValid)) {
                    paymentTriggered = true
                    viewModel.createPayment(
                        ticketId,
                        event.userId,
                        event.price,
                        paymentType = selectedMethod,
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = EventionBlue),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "PAY",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun Preview() {
    EventionTheme {
        val navController = rememberNavController()
        val viewModel: PaymentViewModel = viewModel()
        PaymentScreen(MockData.events.first(),"as", navController, viewModel)
    }
}
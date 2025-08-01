package com.example.evention.model

data class Payment(
    val ticketID: String,
    val event: Event,
)

data class CreatePaymentRequest(
    val ticketID: String,
    val userId: String,
    val totalValue: Number,
    val paymentType: String,
)
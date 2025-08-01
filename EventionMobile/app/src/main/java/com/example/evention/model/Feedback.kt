package com.example.evention.model

data class Feedback(
    val feedbackID: String,
    val rating: Int,
    val commentary: String,
    val userInEvent: Ticket? = null
)
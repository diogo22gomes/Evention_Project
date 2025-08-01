package com.example.evention.model

import com.google.gson.annotations.SerializedName


data class Ticket(
    val ticketID: String,
    val user_id: String? = null,
    val feedback_id:String? = null,
    val participated: Boolean? = null,
    val event: Event,
)

data class TicketRaw(
    val ticketID: String,
    val event_id: String,
    val user_id: String,
    val feedback_id: String,
    val participated: Boolean
)

data class CreateTicketRequest(
    @SerializedName("event_id") val eventId: String
)

data class CreateFeedbackRequest(
    val rating: Int,
    val commentary: String
)
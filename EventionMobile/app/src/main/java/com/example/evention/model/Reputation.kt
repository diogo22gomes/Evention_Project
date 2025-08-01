package com.example.evention.model

data class Reputation(
    val eventCount: Int,
    val reputation: Int,
    val tickets: List<TicketReputation>
)

data class TicketReputation(
    val ticketID: String,
    val event_id: String,
    val user_id: String,
    val feedback_id: String?,
    val createdAt: String,
    val participated: Boolean,
    val feedback: FeedbackReputation?
)

data class FeedbackReputation(
    val feedbackID: String,
    val rating: Int,
    val commentary: String
)
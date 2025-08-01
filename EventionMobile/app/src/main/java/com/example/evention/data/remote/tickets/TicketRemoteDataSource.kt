package com.example.evention.data.remote.tickets

import com.example.evention.model.CreateFeedbackRequest
import com.example.evention.model.CreateTicketRequest
import com.example.evention.model.Feedback
import com.example.evention.model.Ticket
import com.example.evention.model.TicketRaw
import com.example.evention.model.TicketReputation

class TicketRemoteDataSource(private val api: TicketApiService) {
    suspend fun getTickets(): List<TicketRaw> = api.getTickets()

    suspend fun getTicketById(ticketId: String): TicketRaw = api.getTicketById(ticketId)

    suspend fun getTicketsByEvent(eventId: String): List<TicketReputation> = api.getTicketsByEvent(eventId)

    suspend fun createTicket(eventId: String): Ticket {
        val request = CreateTicketRequest(eventId)
        return api.createTicket(request)
    }
    suspend fun createFeedback(eventId: String, request: CreateFeedbackRequest): Feedback {
        return api.createFeedback(eventId, request)
    }
}

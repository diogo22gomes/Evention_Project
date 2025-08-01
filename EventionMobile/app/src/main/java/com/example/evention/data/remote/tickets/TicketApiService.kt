package com.example.evention.data.remote.tickets

import com.example.evention.model.CreateFeedbackRequest
import com.example.evention.model.CreateTicketRequest
import com.example.evention.model.Feedback
import com.example.evention.model.Ticket
import com.example.evention.model.TicketRaw
import com.example.evention.model.TicketReputation
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TicketApiService {
    @GET("userinevent/api/tickets/my")
    suspend fun getTickets(): List<TicketRaw>

    @GET("userinevent/api/tickets/{id}")
    suspend fun getTicketById(@Path("id") ticketId: String): TicketRaw

    @GET("userinevent/api/tickets/event/{id}")
    suspend fun getTicketsByEvent(@Path("id") eventId: String): List<TicketReputation>

    @POST("userinevent/api/tickets")
    suspend fun createTicket(
        @Body request: CreateTicketRequest,
    ): Ticket

    @POST("userinevent/api/feedbacks/{eventId}")
    suspend fun createFeedback(
        @Path("eventId") eventId: String,
        @Body request: CreateFeedbackRequest,
    ): Feedback
}
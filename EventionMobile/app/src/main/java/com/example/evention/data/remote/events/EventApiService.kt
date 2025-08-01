package com.example.evention.data.remote.events

import com.example.evention.model.AddressEventRequest
import com.example.evention.model.AddressEventResponse
import com.example.evention.model.Event
import com.example.evention.model.EventResponse
import com.example.evention.model.Reputation
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.evention.model.RoutesEventRequest
import com.example.evention.model.RoutesEventResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface EventApiService {
    @GET("event/api/events")
    suspend fun getEvents(): List<Event>

    @GET("event/api/events/approved")
    suspend fun getApprovedEvents(): List<Event>

    @GET("event/api/events/reputation/{id}")
    suspend fun getUserReputation(@Path("id") userId: String): Reputation

    @GET("event/api/events/my")
    suspend fun getMyEvents(): List<Event>

    @GET("event/api/events/{id}")
    suspend fun getEventById(@Path("id") eventId: String): Event

    @GET("event/api/events/suspended")
    suspend fun getSuspendedEvents(): List<Event>

    @PUT("event/api/events/{id}/status")
    suspend fun approveEvent(@Path("id") eventId: String): Event

    @DELETE("event/api/events/{id}")
    suspend fun deleteEvent(@Path("id") eventId: String)

    @Multipart
    @PUT("event/api/events/{id}")
    suspend fun updateEvent(
        @Path("id") eventId: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("startAt") startAt: RequestBody,
        @Part("endAt") endAt: RequestBody,
        @Part("price") price: RequestBody,
        @Part eventPicture: MultipartBody.Part? = null
    ): Event

    @POST("event/api/addressEvents")
    suspend fun createAddressEvent(@Body request: AddressEventRequest): Response<AddressEventResponse>

    @POST("event/api/routesEvents")
    suspend fun createRouteEvent(@Body request: RoutesEventRequest): Response<RoutesEventResponse>

    @Multipart
    @POST("event/api/events")
    suspend fun createEventWithImage(
        @Part("userId") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("startAt") startAt: RequestBody,
        @Part("endAt") endAt: RequestBody,
        @Part("price") price: RequestBody,
        @Part("eventStatusID") eventStatusID: RequestBody,
        @Part eventPicture: MultipartBody.Part?
    ): Response<EventResponse>
}
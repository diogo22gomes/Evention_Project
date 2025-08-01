package com.example.evention.di

import LoginApiService
import LoginRemoteDataSource
import com.example.evention.data.remote.authentication.RegisterApiService
import com.example.evention.data.remote.authentication.RegisterRemoteDataSource
import UserPreferences
import com.example.evention.data.remote.authentication.ResetPasswordRemoteDataSource
import com.example.evention.data.remote.events.EventApiService
import com.example.evention.data.remote.events.EventRemoteDataSource
import com.example.evention.data.remote.location.LocationApiService
import com.example.evention.data.remote.location.LocationRemoteDataSource
import com.example.evention.data.remote.payments.PaymentApiService
import com.example.evention.data.remote.payments.PaymentRemoteDataSource
import com.example.evention.data.remote.tickets.TicketApiService
import com.example.evention.data.remote.tickets.TicketRemoteDataSource
import getUnsafeOkHttpClient
import com.example.evention.data.remote.users.UserApiService
import com.example.evention.data.remote.users.UserRemoteDataSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://10.0.2.2:5010/"

    private lateinit var userPreferences: UserPreferences

    fun init(userPreferences: UserPreferences) {
        this.userPreferences = userPreferences
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getUnsafeOkHttpClient(userPreferences))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API
    private val eventApi: EventApiService by lazy {
        retrofit.create(EventApiService::class.java)
    }

    private val ticketApi: TicketApiService by lazy {
        retrofit.create(TicketApiService::class.java)
    }

    private val paymentApi: PaymentApiService by lazy {
        retrofit.create(PaymentApiService::class.java)
    }

    private val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    private val loginApi: LoginApiService by lazy {
        retrofit.create(LoginApiService::class.java)
    }

    private val registerApi: RegisterApiService by lazy {
        retrofit.create(RegisterApiService::class.java)
    }

    private val locationApi: LocationApiService by lazy {
        retrofit.create(LocationApiService::class.java)
    }

    // Data Source
    val eventRemoteDataSource: EventRemoteDataSource by lazy {
        EventRemoteDataSource(eventApi)
    }

    val paymentRemoteDataSource: PaymentRemoteDataSource by lazy {
        PaymentRemoteDataSource(paymentApi)
    }

    val ticketRemoteDataSource: TicketRemoteDataSource by lazy {
        TicketRemoteDataSource(ticketApi)
    }

    val userRemoteDataSource: UserRemoteDataSource by lazy {
        UserRemoteDataSource(userApi)
    }

    val loginRemoteDataSource: LoginRemoteDataSource by lazy {
        LoginRemoteDataSource(loginApi)
    }

    val registerRemoteDataSource: RegisterRemoteDataSource by lazy {
        RegisterRemoteDataSource(registerApi)
    }

    val resetPasswordRemoteDataSource: ResetPasswordRemoteDataSource by lazy {
        ResetPasswordRemoteDataSource(loginApi)
    }

    val locationRemoteDataSource: LocationRemoteDataSource by lazy {
        LocationRemoteDataSource(locationApi)
    }

}
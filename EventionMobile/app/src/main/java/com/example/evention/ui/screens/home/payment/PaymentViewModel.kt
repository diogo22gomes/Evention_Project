package com.example.evention.ui.screens.home.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Payment
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PaymentViewModel : ViewModel() {

    private val remoteDataSource = NetworkModule.paymentRemoteDataSource

    private val _paymentResult = MutableStateFlow<Payment?>(null)
    val paymentResult: StateFlow<Payment?> = _paymentResult

    private val _errorMessage = MutableStateFlow<String?>(null)

    fun createPayment(
        ticketID: String,
        userId: String,
        totalValue: Number,
        paymentType: String,
    ) {

        viewModelScope.launch {
            try {
                val payment = remoteDataSource.createPayment(
                    ticketID, userId, totalValue, paymentType
                )
                _paymentResult.value = payment
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }
}
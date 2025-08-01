package com.example.evention.data.local.factory

import TicketRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.evention.ui.screens.ticket.TicketDetailsScreenViewModel

class TicketDetailsViewModelFactory(
    private val repository: TicketRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicketDetailsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TicketDetailsScreenViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

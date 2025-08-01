package com.example.evention.ui.screens.ticket

import TicketRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Ticket
import com.example.evention.utils.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TicketDetailsScreenViewModel(
    private val repository: TicketRepository
) : ViewModel() {

    private val ticketRemote = NetworkModule.ticketRemoteDataSource
    private val eventRemote = NetworkModule.eventRemoteDataSource

    private val _ticket = MutableStateFlow<Ticket?>(null)
    val ticket: StateFlow<Ticket?> = _ticket

    fun loadTicketById(ticketId: String) {
        viewModelScope.launch {
            try {
                val remoteTicket = ticketRemote.getTicketById(ticketId)
                val remoteEvent = eventRemote.getEventById(remoteTicket.event_id)

                _ticket.value = Ticket(
                    ticketID = remoteTicket.ticketID,
                    user_id = remoteTicket.user_id,
                    feedback_id = remoteTicket.feedback_id,
                    participated = remoteTicket.participated,
                    event = remoteEvent
                )

                repository.syncTickets()

            } catch (e: Exception) {
                val localTicket = repository.getTicketById(ticketId)
                val localEvent = localTicket?.let { repository.getLocalEventById(it.event_id) }

                if (localTicket != null && localEvent != null) {
                    _ticket.value = Ticket(
                        ticketID = localTicket.ticketID,
                        user_id = localTicket.user_id,
                        feedback_id = localTicket.feedback_id,
                        participated = localTicket.participated,
                        event = localEvent.toDomain()
                    )
                } else {
                    _ticket.value = null
                }
            }
        }
    }

}

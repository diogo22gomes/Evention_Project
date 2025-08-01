package com.example.evention.ui.screens.profile.userEvents.userParticipation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.TicketReputation
import com.example.evention.model.User
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TicketWithUser(
    val ticket: TicketReputation,
    val user: User
)
class UserParticipationViewModel : ViewModel() {

    private val ticketRemoteDataSource = NetworkModule.ticketRemoteDataSource
    private val userRemoteDataSource = NetworkModule.userRemoteDataSource

    private val _ticketsWithUsers = MutableStateFlow<List<TicketWithUser>>(emptyList())
    val ticketsWithUsers: StateFlow<List<TicketWithUser>> = _ticketsWithUsers

    fun loadTickets(eventId: String) {
        viewModelScope.launch {
            try {
                val tickets = ticketRemoteDataSource.getTicketsByEvent(eventId)

                val ticketWithUsers = tickets.map { ticket ->
                    async {
                        val user = userRemoteDataSource.getUserById(ticket.user_id)
                        TicketWithUser(ticket, user)
                    }
                }.awaitAll()

                _ticketsWithUsers.value = ticketWithUsers

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

package com.example.evention.ui.screens.ticket

import TicketRepository
import UserPreferences
import android.util.Log
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evention.di.NetworkModule
import com.example.evention.model.Ticket
import com.example.evention.utils.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TicketScreenViewModel(
    private val repository: TicketRepository,
    userPreferences: UserPreferences? = null
) : ViewModel() {

    private val _createTicketResult = MutableStateFlow<Result<Unit>?>(null)

    private val _ticketId = MutableStateFlow<String?>(null)
    val ticketId: StateFlow<String?> = _ticketId

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    private val remoteDataSource = NetworkModule.eventRemoteDataSource
    private val ticketRemoteDataSource = NetworkModule.ticketRemoteDataSource

    private val userId = userPreferences?.getUserId()

    init {
        viewModelScope.launch {
            try {
                repository.syncTickets()
            } catch (e: Exception) {
                Log.w("TicketScreenVM", "Sync failed, using local data", e)
            }

            if (userId != null) {
                repository.getLocalTickets(userId).collect { localTickets ->
                    val ticketsWithEvents = localTickets.map { entity ->
                        val event = try {
                            remoteDataSource.getEventById(entity.event_id)
                        } catch (e: Exception) {
                            repository.getLocalEventById(entity.event_id)?.toDomain()
                        }

                        Ticket(
                            ticketID = entity.ticketID,
                            event = event!!
                        )
                    }
                    _tickets.value = ticketsWithEvents
                }
            }
        }
    }

    fun createTicket(eventId: String) {
        viewModelScope.launch {
            try {
                val ticket = ticketRemoteDataSource.createTicket(eventId)
                _createTicketResult.value = Result.success(Unit)
                _ticketId.value = ticket.ticketID
            } catch (e: Exception) {
                _createTicketResult.value = Result.failure(e)
            }
        }
    }

    fun clearCreateResult() {
        _createTicketResult.value = null
    }
}



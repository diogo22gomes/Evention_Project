import com.example.evention.data.local.dao.TicketDao
import com.example.evention.data.local.entities.EventEntity
import com.example.evention.data.local.entities.TicketEntity
import com.example.evention.data.remote.events.EventRemoteDataSource
import com.example.evention.data.remote.tickets.TicketRemoteDataSource
import kotlinx.coroutines.flow.Flow

class TicketRepository(
    private val remote: TicketRemoteDataSource,
    private val eventRemote: EventRemoteDataSource,
    private val local: TicketDao
) {
    fun getLocalTickets(userId: String): Flow<List<TicketEntity>> = local.getUserTickets(userId)

    suspend fun syncTickets() {
        val remoteTickets = remote.getTickets()

        val localTicketEntities = remoteTickets.map { ticket ->
            TicketEntity(
                ticketID = ticket.ticketID,
                event_id = ticket.event_id,
                user_id = ticket.user_id,
                feedback_id = ticket.feedback_id,
                participated = ticket.participated
            )
        }

        val uniqueEventIds = remoteTickets.map { it.event_id }.toSet()

        val localEventEntities = uniqueEventIds.mapNotNull { eventId ->
            try {
                val remoteEvent = eventRemote.getEventById(eventId)
                EventEntity(
                    eventID = remoteEvent.eventID,
                    userId = remoteEvent.userId,
                    name = remoteEvent.name,
                    description = remoteEvent.description,
                    startAt = remoteEvent.startAt.toString(),
                    endAt = remoteEvent.endAt.toString(),
                    price = remoteEvent.price,
                    eventPicture = remoteEvent.eventPicture
                )
            } catch (e: Exception) {
                null
            }
        }

        local.insertAll(localTicketEntities)
        local.insertAllEvents(localEventEntities)
    }

    suspend fun getLocalEventById(eventId: String): EventEntity? {
        return local.getEventById(eventId)
    }

    suspend fun getTicketById(ticketId: String): TicketEntity? {
        return local.getTicketById(ticketId)
    }
}
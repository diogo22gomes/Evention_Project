package com.example.evention.mock

import com.example.evention.model.Ticket

object TicketMockData {

    public val tickets = listOf(
    Ticket(ticketID = "1", event = MockData.events[0]) ,
    Ticket(ticketID = "2", event = MockData.events[1]),
    Ticket(ticketID = "3", event = MockData.events[2]),
    Ticket(ticketID = "4", event = MockData.events[3]),
    Ticket(ticketID = "5", event = MockData.events[4]),
    )
}
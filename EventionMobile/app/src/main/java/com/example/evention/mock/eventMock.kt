package com.example.evention.mock

import com.example.evention.model.*
import java.util.*

object MockData {

    private val addressEvents = listOf(
        AddressEvent(
            addressEstablishmentID = "1",
            road = "Rua Central",
            roadNumber = 123,
            postCode = "4750-123",
            localtown = "Barcelos",
            routes = listOf(
                RoutesEvent("r1", 41.5381, -8.6151, 1)
            )
        ),
        AddressEvent(
            addressEstablishmentID = "2",
            road = "Avenida Principal",
            roadNumber = 456,
            postCode = "4750-456",
            localtown = "Braga",
            routes = listOf(
                RoutesEvent("r2", 38.7169, -9.1399, 1)
            )
        ),
        AddressEvent(
            addressEstablishmentID = "3",
            road = "Rua da Liberdade",
            roadNumber = 789,
            postCode = "4700-789",
            localtown = "Guimarães",
            routes = listOf(
                RoutesEvent("r3", 41.445, -8.2906, 1)
            )
        ),
        AddressEvent(
            addressEstablishmentID = "4",
            road = "Avenida Marginal",
            roadNumber = 101,
            postCode = "4900-101",
            localtown = "Viana do Castelo",
            routes = listOf(
                RoutesEvent("r4", 41.6918, -8.8344, 1)
            )
        )
    )

    private val eventStatuses = listOf(
        EventStatus(
            eventStatusID = "1",
            status = "Aprovado"
        ),
        EventStatus(
            eventStatusID = "2",
            status = "Pendente"
        ),
        EventStatus(
            eventStatusID = "3",
            status = "Completo"
        )
    )

    val events = listOf(
        Event(
            eventID = "e1",
            userId = "u1",
            name = "Festival de Música",
            description = "O maior festival da região!",
            startAt = Date(),
            endAt = Date(),
            price = 20.0,
            createdAt = Date(),
            eventStatus = eventStatuses[0],
            addressEvents = addressEvents,
            eventPicture = "event1"
        ),
        Event(
            eventID = "e2",
            userId = "u2",
            name = "Feira de Tecnologia",
            description = "Exposição de novas tecnologias.",
            startAt = Date(),
            endAt = Date(),
            price = 0.0,
            createdAt = Date(),
            eventStatus = eventStatuses[1],
            addressEvents = listOf(addressEvents[0]),
            eventPicture = "event2"
        ),
        Event(
            eventID = "e3",
            userId = "u3",
            name = "Festival Gastronómico",
            description = "Prove o melhor da cozinha tradicional.",
            startAt = Date(),
            endAt = Date(),
            price = 12.5,
            createdAt = Date(),
            eventStatus = eventStatuses[2],
            addressEvents = listOf(addressEvents[2]),
            eventPicture = "event3"
        ),
        Event(
            eventID = "e4",
            userId = "u4",
            name = "Workshop de Fotografia",
            description = "Aprenda técnicas incríveis de fotografia.",
            startAt = Date(),
            endAt = Date(),
            price = 30.0,
            createdAt = Date(),
            eventStatus = eventStatuses[0],
            addressEvents = listOf(addressEvents[1]),
            eventPicture = "event4"
        ),
        Event(
            eventID = "e5",
            userId = "u5",
            name = "Corrida Solidária",
            description = "Evento de angariação de fundos.",
            startAt = Date(),
            endAt = Date(),
            price = 5.0,
            createdAt = Date(),
            eventStatus = eventStatuses[1],
            addressEvents = listOf(addressEvents[3]),
            eventPicture = "event5"
        ),
        Event(
            eventID = "e6",
            userId = "u6",
            name = "Mostra de Cinema",
            description = "Os melhores filmes independentes.",
            startAt = Date(),
            endAt = Date(),
            price = 7.5,
            createdAt = Date(),
            eventStatus = eventStatuses[0],
            addressEvents = listOf(addressEvents[0]),
            eventPicture = "event6"
        ),
        Event(
            eventID = "e7",
            userId = "u7",
            name = "Feira Medieval",
            description = "Volte atrás no tempo!",
            startAt = Date(),
            endAt = Date(),
            price = 10.0,
            createdAt = Date(),
            eventStatus = eventStatuses[2],
            addressEvents = listOf(addressEvents[2]),
            eventPicture = "event7"
        ),
        Event(
            eventID = "e8",
            userId = "u8",
            name = "Conferência de Saúde",
            description = "Atualize-se com os novos avanços médicos.",
            startAt = Date(),
            endAt = Date(),
            price = 15.0,
            createdAt = Date(),
            eventStatus = eventStatuses[1],
            addressEvents = listOf(addressEvents[1]),
            eventPicture = "event8"
        ),
        Event(
            eventID = "e9",
            userId = "u9",
            name = "Encontro de Carros Antigos",
            description = "Exposição de clássicos automóveis.",
            startAt = Date(),
            endAt = Date(),
            price = 8.0,
            createdAt = Date(),
            eventStatus = eventStatuses[0],
            addressEvents = listOf(addressEvents[3]),
            eventPicture = "event9"
        ),
        Event(
            eventID = "e10",
            userId = "u10",
            name = "Festival de Jazz",
            description = "Grandes nomes do Jazz nacional e internacional.",
            startAt = Date(),
            endAt = Date(),
            price = 18.0,
            createdAt = Date(),
            eventStatus = eventStatuses[2],
            addressEvents = listOf(addressEvents[0]),
            eventPicture = "event10"
        )
    )
}

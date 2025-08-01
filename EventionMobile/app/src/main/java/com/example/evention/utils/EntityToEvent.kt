package com.example.evention.utils

import com.example.evention.data.local.entities.EventEntity
import com.example.evention.model.AddressEvent
import com.example.evention.model.Event
import com.example.evention.model.EventStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun EventEntity.toDomain(): Event {
    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    val startDate = dateFormat.parse(this.startAt)

    val endDate = dateFormat.parse(this.endAt)

    return Event(
        eventID = this.eventID,
        userId = this.userId,
        name = this.name,
        description = this.description,
        startAt = startDate!!,
        endAt = endDate!!,
        price = this.price,
        eventPicture = this.eventPicture,
        createdAt = Date(),
        eventStatus = EventStatus(
            eventStatusID = "",
            status = ""
        ),
        addressEvents = listOf<AddressEvent>()
    )
}
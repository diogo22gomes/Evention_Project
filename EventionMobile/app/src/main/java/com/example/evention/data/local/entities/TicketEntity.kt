package com.example.evention.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets")
data class TicketEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ticket_id")
    val ticketID: String,

    @ColumnInfo(name = "event_id")
    val event_id: String,

    @ColumnInfo(name = "user_id")
    val user_id: String,

    @ColumnInfo(name = "feedback_id")
    val feedback_id: String?,

    @ColumnInfo(name = "participated")
    val participated: Boolean
)

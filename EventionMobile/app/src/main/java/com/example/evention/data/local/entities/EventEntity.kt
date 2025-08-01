package com.example.evention.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.evention.model.EventStatus
import java.util.Date

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    @ColumnInfo(name = "event_id")
    val eventID: String,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "start_at")
    val startAt: String,

    @ColumnInfo(name = "end_at")
    val endAt: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "event_picture")
    val eventPicture: String?,
)

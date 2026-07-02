package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val mobileNumber: String,
    val pickupLocation: String,
    val dropLocation: String,
    val dateTime: String,
    val vehicleType: String,
    val specialNotes: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Pending" // "Pending", "Confirmed", "Completed", "Cancelled"
) : Serializable

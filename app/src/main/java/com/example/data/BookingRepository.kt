package com.example.data

import kotlinx.coroutines.flow.Flow

class BookingRepository(private val bookingDao: BookingDao) {
    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings()

    suspend fun insert(booking: Booking): Long {
        return bookingDao.insertBooking(booking)
    }

    suspend fun deleteById(id: Int) {
        bookingDao.deleteBookingById(id)
    }

    suspend fun updateStatus(id: Int, status: String) {
        bookingDao.updateBookingStatus(id, status)
    }
}

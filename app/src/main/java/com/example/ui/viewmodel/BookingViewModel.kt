package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.Booking
import com.example.data.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookingRepository

    val allBookings: StateFlow<List<Booking>>

    // Dark Mode setting (null means follow system, true/false overrides)
    private val _isDarkMode = MutableStateFlow<Boolean?>(null)
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode.asStateFlow()

    // Form inputs state
    private val _formName = MutableStateFlow("")
    val formName = _formName.asStateFlow()

    private val _formMobile = MutableStateFlow("")
    val formMobile = _formMobile.asStateFlow()

    private val _formPickup = MutableStateFlow("")
    val formPickup = _formPickup.asStateFlow()

    private val _formDrop = MutableStateFlow("")
    val formDrop = _formDrop.asStateFlow()

    private val _formDateTime = MutableStateFlow("")
    val formDateTime = _formDateTime.asStateFlow()

    private val _formVehicleType = MutableStateFlow("Sedan")
    val formVehicleType = _formVehicleType.asStateFlow()

    private val _formNotes = MutableStateFlow("")
    val formNotes = _formNotes.asStateFlow()

    // Error states
    private val _formErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val formErrors = _formErrors.asStateFlow()

    // Dialog & notification states
    private val _bookingSuccess = MutableStateFlow(false)
    val bookingSuccess = _bookingSuccess.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = BookingRepository(database.bookingDao())
        allBookings = repository.allBookings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun setDarkMode(enabled: Boolean?) {
        _isDarkMode.value = enabled
    }

    fun onNameChange(value: String) {
        _formName.value = value
        clearError("name")
    }

    fun onMobileChange(value: String) {
        _formMobile.value = value
        clearError("mobile")
    }

    fun onPickupChange(value: String) {
        _formPickup.value = value
        clearError("pickup")
    }

    fun onDropChange(value: String) {
        _formDrop.value = value
        clearError("drop")
    }

    fun onDateTimeChange(value: String) {
        _formDateTime.value = value
        clearError("datetime")
    }

    fun onVehicleTypeChange(value: String) {
        _formVehicleType.value = value
    }

    fun onNotesChange(value: String) {
        _formNotes.value = value
    }

    private fun clearError(field: String) {
        _formErrors.value = _formErrors.value.toMutableMap().apply { remove(field) }
    }

    fun validateAndBook(): Boolean {
        val errors = mutableMapOf<String, String>()
        
        if (_formName.value.trim().isEmpty()) {
            errors["name"] = "Name is required"
        }
        if (_formMobile.value.trim().isEmpty()) {
            errors["mobile"] = "Mobile number is required"
        } else if (_formMobile.value.trim().length < 10) {
            errors["mobile"] = "Enter a valid 10-digit mobile number"
        }
        if (_formPickup.value.trim().isEmpty()) {
            errors["pickup"] = "Pickup location is required"
        }
        if (_formDrop.value.trim().isEmpty()) {
            errors["drop"] = "Drop location is required"
        }
        if (_formDateTime.value.trim().isEmpty()) {
            errors["datetime"] = "Pickup date and time is required"
        }

        if (errors.isNotEmpty()) {
            _formErrors.value = errors
            return false
        }

        viewModelScope.launch {
            val booking = Booking(
                name = _formName.value.trim(),
                mobileNumber = _formMobile.value.trim(),
                pickupLocation = _formPickup.value.trim(),
                dropLocation = _formDrop.value.trim(),
                dateTime = _formDateTime.value.trim(),
                vehicleType = _formVehicleType.value,
                specialNotes = _formNotes.value.trim()
            )
            repository.insert(booking)
            _bookingSuccess.value = true
            resetForm()
        }
        return true
    }

    fun dismissBookingSuccess() {
        _bookingSuccess.value = false
    }

    fun cancelBooking(id: Int) {
        viewModelScope.launch {
            repository.updateStatus(id, "Cancelled")
        }
    }

    private fun resetForm() {
        _formName.value = ""
        _formMobile.value = ""
        _formPickup.value = ""
        _formDrop.value = ""
        _formDateTime.value = ""
        _formVehicleType.value = "Sedan"
        _formNotes.value = ""
        _formErrors.value = emptyMap()
    }
}

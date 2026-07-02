package com.example.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.BookingViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookRideScreen(
    viewModel: BookingViewModel,
    onNavigateToBookings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Form fields observed from ViewModel
    val name by viewModel.formName.collectAsState()
    val mobile by viewModel.formMobile.collectAsState()
    val pickup by viewModel.formPickup.collectAsState()
    val drop by viewModel.formDrop.collectAsState()
    val dateTime by viewModel.formDateTime.collectAsState()
    val vehicleType by viewModel.formVehicleType.collectAsState()
    val notes by viewModel.formNotes.collectAsState()
    val errors by viewModel.formErrors.collectAsState()
    val bookingSuccess by viewModel.bookingSuccess.collectAsState()

    // Dropdown list
    val vehiclesList = remember { listOf("Sedan", "SUV", "Ertiga", "Innova", "Tempo Traveller") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Date Time picker launcher
    val calendar = remember { Calendar.getInstance() }
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                
                // Once date is picked, launch time picker
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        
                        val formattedDateTime = String.format(
                            Locale.US,
                            "%02d/%02d/%d %02d:%02d",
                            dayOfMonth, month + 1, year, hourOfDay, minute
                        )
                        viewModel.onDateTimeChange(formattedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    if (bookingSuccess) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissBookingSuccess() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF25D366),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Booking Confirmed!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Thank you for choosing Mahadev Travels.",
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your offline travel booking request has been securely saved. Our booking desk will contact you shortly to confirm driver details.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissBookingSuccess()
                        onNavigateToBookings()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("View My Rides")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissBookingSuccess() }) {
                    Text("OK", color = MaterialTheme.colorScheme.primary)
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("book_ride_container")
    ) {
        // Form Title
        Text(
            text = "Book Your Ride",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag("booking_screen_title")
        )
        Text(
            text = "Fill in the details below. We guarantee timely pick-ups.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // 1. Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Your Name") },
            placeholder = { Text("Enter your full name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = errors.containsKey("name"),
            supportingText = {
                errors["name"]?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_name"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Mobile Field
        OutlinedTextField(
            value = mobile,
            onValueChange = { viewModel.onMobileChange(it) },
            label = { Text("Mobile Number") },
            placeholder = { Text("Enter 10-digit mobile number") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            isError = errors.containsKey("mobile"),
            supportingText = {
                errors["mobile"]?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_mobile"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Pickup Location
        OutlinedTextField(
            value = pickup,
            onValueChange = { viewModel.onPickupChange(it) },
            label = { Text("Pickup Address") },
            placeholder = { Text("e.g. Gita Mandir, Ahmedabad Airport, Kalupur") },
            leadingIcon = { Icon(Icons.Default.MyLocation, contentDescription = null) },
            isError = errors.containsKey("pickup"),
            supportingText = {
                errors["pickup"]?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_pickup"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 4. Drop Location
        OutlinedTextField(
            value = drop,
            onValueChange = { viewModel.onDropChange(it) },
            label = { Text("Drop-off Address") },
            placeholder = { Text("e.g. Statue of Unity, Vadodara, Rajkot, Bopal") },
            leadingIcon = { Icon(Icons.Default.PinDrop, contentDescription = null) },
            isError = errors.containsKey("drop"),
            supportingText = {
                errors["drop"]?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_drop"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 5. Date & Time Picker
        OutlinedTextField(
            value = dateTime,
            onValueChange = {},
            label = { Text("Pickup Date & Time") },
            placeholder = { Text("Tap to select date and time") },
            leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
            readOnly = true,
            isError = errors.containsKey("datetime"),
            supportingText = {
                errors["datetime"]?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() }
                .testTag("input_datetime"),
            enabled = false, // Prevents keyboard from rising and relies on click
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (errors.containsKey("datetime")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 6. Vehicle Category Dropdown Selector
        ExposedDropdownMenuBox(
            expanded = dropdownExpanded,
            onExpandedChange = { dropdownExpanded = !dropdownExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = vehicleType,
                onValueChange = {},
                readOnly = true,
                label = { Text("Vehicle Type") },
                leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .testTag("input_vehicle_type"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
            ExposedDropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false }
            ) {
                vehiclesList.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            viewModel.onVehicleTypeChange(selectionOption)
                            dropdownExpanded = false
                        },
                        modifier = Modifier.testTag("vehicle_option_$selectionOption")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7. Special Notes
        OutlinedTextField(
            value = notes,
            onValueChange = { viewModel.onNotesChange(it) },
            label = { Text("Special Requests / Flight Details (Optional)") },
            placeholder = { Text("e.g. Baby seat required, extra luggage, one-way request...") },
            leadingIcon = { Icon(Icons.Default.Note, contentDescription = null) },
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_notes"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Confirm Button
        Button(
            onClick = { viewModel.validateAndBook() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("confirm_booking_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "CONFIRM VEHICLE BOOKING",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

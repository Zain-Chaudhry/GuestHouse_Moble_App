package com.example.diamondguesthouse.userinterface.components.textfield

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diamondguesthouse.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun EmailTextField(

    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }

    OutlinedTextField(
        value = email ,
        onValueChange = {email = it},
        shape = RoundedCornerShape(32.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(text = label, color = MaterialTheme.colorScheme.onBackground) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        modifier = modifier
        )
}

@Composable
fun PasswordTextField(
    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember { PasswordVisualTransformation() }

    OutlinedTextField(
        value = password ,
        onValueChange = { password = it},
        shape = RoundedCornerShape( 32.dp) ,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = { Text(text = label, color = MaterialTheme.colorScheme.onBackground) },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            passwordVisualTransformation
        },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        modifier = modifier,
        trailingIcon = {
            Icon(
                if (showPassword) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                },
                contentDescription = "Toggle password visibility",
                modifier = Modifier.clickable { showPassword =!showPassword })

        }
    )
}

@Composable
fun NameTextField(

    label: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }

    OutlinedTextField(
        value = name ,
        onValueChange = {name = it},
        shape = RoundedCornerShape(32.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        label = { Text(text = label, color = MaterialTheme.colorScheme.onBackground) },
        leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
        modifier = modifier
    )
}


// This is for the AddRecord.kt file
enum class ValidationType {
    CNIC,
    PASSPORT,
    MOBILE
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    validationType: ValidationType? = null
) {
    var text by remember { mutableStateOf(value) }
    var errorMessage by remember { mutableStateOf("") }
    Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)

    fun validate(input: String): String {
        return when (validationType) {
            ValidationType.CNIC -> {
                // CNIC: XXXXX-XXXXXXX-X (15 characters including dashes)
                if (Regex("\\d{5}-\\d{7}-\\d").matches(input)) {
                    ""
                } else {
                    "Invalid CNIC format"
                }
            }
            ValidationType.PASSPORT -> {
                // Passport: 8-9 alphanumeric characters
                if (Regex("^[A-Za-z0-9]{8,9}$").matches(input)) {
                    ""
                } else {
                    "Invalid Passport format"
                }
            }
            ValidationType.MOBILE -> {
                // Mobile: 11 digits starting with 03
                if (Regex("^03\\d{9}$").matches(input)) {
                    ""
                } else {
                    "Invalid Mobile number"
                }
            }
            else -> ""
        }
    }


    fun handleValueChange(newValue: String) {
        text = newValue
        onValueChange(newValue)
        errorMessage = validate(newValue)
    }


    OutlinedTextField(
        value = text,
        onValueChange = { handleValueChange(it) },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth()
    )
    if (errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
    Spacer(modifier = Modifier.size(10.dp))
}

// Field to display Time, i.e. Check in time, Check out time
@Composable
fun TimeField(label: String, value: Long, onTimeSelected: (Long) -> Unit, isClickable: Boolean) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // If value is a timestamp, convert it to a human-readable format for display
    val formattedTime = if (value != 0L) {
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(value))
    } else {
        "Please Select"
    }

    Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    Spacer(modifier = Modifier.size(10.dp))

    if (isClickable) {
        OutlinedTextField(
            value = formattedTime,
            onValueChange = { /* No-op: we don't want to change text directly */ },
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                // Set the calendar to selected time
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)
                                // Convert to Unix timestamp and pass the selected time
                                val selectedTime = calendar.timeInMillis
                                onTimeSelected(selectedTime)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(10.dp))
    } else {
        OutlinedTextField(
            value = formattedTime,
            onValueChange = { /* No-op */ },
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

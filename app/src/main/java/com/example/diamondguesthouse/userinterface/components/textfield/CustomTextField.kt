package com.example.diamondguesthouse.userinterface.components.textfield

import android.graphics.drawable.Icon
import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import perfetto.protos.FieldDescriptorProto

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

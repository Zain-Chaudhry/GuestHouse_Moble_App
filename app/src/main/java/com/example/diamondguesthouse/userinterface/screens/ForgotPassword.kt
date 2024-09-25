package com.example.diamondguesthouse.userinterface.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.userinterface.components.buttons.PrimaryButton
import com.example.diamondguesthouse.userinterface.components.textfield.EmailTextField
import com.example.diamondguesthouse.viewmodel.AuthState
import com.example.diamondguesthouse.viewmodel.AuthViewModel

@Composable
fun ForgotPasswordScreen(authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Success -> {
                Toast.makeText(context, (authState.value as AuthState.Success).message, Toast.LENGTH_SHORT).show()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Reset your Password",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Forgot Password?",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                        )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Enter your Email address and we will send you instructions to reset your password.",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                        )
                    Spacer(modifier = Modifier.height(25.dp))
                    EmailTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        leadingIcon = Icons.Default.Email,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    PrimaryButton(
                        text = "Submit",
                        fontSize = 18.sp,
                        onClick = { authViewModel.resetPassword(email) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp))
                }

            }
        }

    }
}

@Preview(showBackground = true)
@Composable

fun ForgotPasswordPreview(){
    DiamondGuestHouseTheme {
        ForgotPasswordScreen(authViewModel = AuthViewModel())
    }
}
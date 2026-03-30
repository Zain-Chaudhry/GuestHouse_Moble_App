package com.example.diamondguesthouse.presentation.sign_up_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.appNavigation.GuestHouseNavKey
import com.example.diamondguesthouse.appNavigation.NavCommand
import com.example.diamondguesthouse.appNavigation.OnGuestHouseNavigate
import com.example.diamondguesthouse.domain.models.AuthState
import com.example.diamondguesthouse.core.presentation.PrimaryButton
import com.example.diamondguesthouse.core.presentation.PrimaryTextButton
import com.example.diamondguesthouse.core.presentation.EmailTextField
import com.example.diamondguesthouse.core.presentation.GenericTextView
import com.example.diamondguesthouse.core.presentation.NameTextField
import com.example.diamondguesthouse.core.presentation.PasswordTextField
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onNavigate: OnGuestHouseNavigate,
    viewModel: SignUpScreenViewModel = koinViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (authState) {
            AuthState.Authenticated -> onNavigate(NavCommand.ReplaceRoot(GuestHouseNavKey.Home))
            is AuthState.Error -> Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            GenericTextView(
                text = "Sign Up to get started",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(14.dp))
                    GenericTextView(
                        text = "Sign Up",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    NameTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Enter Name",
                        leadingIcon = Icons.Default.Person,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    EmailTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Enter Email",
                        leadingIcon = Icons.Default.Email,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Enter Password",
                        leadingIcon = Icons.Default.Lock,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PasswordTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = "Confim Password",
                        leadingIcon = Icons.Default.Lock,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(
                        text = "Sign Up",
                        fontSize = 18.sp,
                        onClick = {
                            viewModel.submitUserEvent(SignUpUserEvent.Submit(name, email, password))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        enabled = authState != AuthState.Loading,
                    )
                    Spacer(modifier = Modifier.height(29.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        GenericTextView(
                            text = "or Sign Up with",
                            modifier = Modifier.padding(horizontal = 8.dp),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "SignUp with google",
                        modifier = Modifier
                            .size(42.dp)
                            .clickable {
                                viewModel.submitUserEvent(SignUpUserEvent.GoogleSignIn(context))
                            },
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        GenericTextView(text = "Already have an account?", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        PrimaryTextButton(
                            text = "Log In",
                            fontSize = 16.sp,
                            onClick = { onNavigate(NavCommand.Pop) },
                        )
                    }
                }
            }
        }
    }
}

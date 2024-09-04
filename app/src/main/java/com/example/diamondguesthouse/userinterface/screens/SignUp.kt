package com.example.diamondguesthouse.userinterface.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.diamondguesthouse.NavRoutes
import com.example.diamondguesthouse.R
import com.example.diamondguesthouse.ui.theme.DiamondGuestHouseTheme
import com.example.diamondguesthouse.userinterface.components.buttons.PrimaryButton
import com.example.diamondguesthouse.userinterface.components.buttons.PrimaryTextButton
import com.example.diamondguesthouse.userinterface.components.textfield.EmailTextField
import com.example.diamondguesthouse.userinterface.components.textfield.NameTextField
import com.example.diamondguesthouse.userinterface.components.textfield.PasswordTextField

@Composable
fun SignUp(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sign Up to get started",
                style = MaterialTheme.typography.titleLarge,
//                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Adds space between the text and the box

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)) // Rounded corners for the box
                    .background(MaterialTheme.colorScheme.background) // Background color for the box
                    .padding(16.dp) // Padding inside the box
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.titleLarge,
//                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    NameTextField(
                        label = "Enter Name",
                        leadingIcon = Icons.Default.Person,
                        modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(14.dp))

                    EmailTextField(
                        label = "Enter Email",
                        leadingIcon = Icons.Default.Email,
                        modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordTextField(
                        label = "Enter Password",
                        leadingIcon = Icons.Default.Lock,
                        Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(16.dp))

                    PasswordTextField(
                        label = "Confim Password",
                        leadingIcon = Icons.Default.Lock,
                        Modifier.fillMaxWidth())


                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryButton(
                        text = "Sign Up",
                        fontSize = 18.sp,
                        onClick = { navController.navigate(NavRoutes.Home) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp))

                    Spacer(modifier = Modifier.height(29.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )


                        Text(
                            text = "or Sign Up with",
                            modifier = Modifier.padding(horizontal = 8.dp), // Adjusts space between text and dividers
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                            thickness = 1.dp,
                            MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Login with google",
                        modifier = Modifier
                            .size(42.dp)
                            .clickable { navController.navigate(NavRoutes.Home) })

                    Spacer(modifier = Modifier.height(20.dp))


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(text = "Already have an account?", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                        PrimaryTextButton(
                            text = "Log In",
                            fontSize = 16.sp,
                            onClick = { navController.navigate(NavRoutes.Login) })


                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview(){
    DiamondGuestHouseTheme {
        SignUp(rememberNavController())
    }
}
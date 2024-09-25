package com.example.diamondguesthouse.userinterface.components.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

@Composable
fun PrimaryButton(
    text: String,
    fontSize: TextUnit,
    onClick: ()-> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
        ) {
        Text(text = text, color = MaterialTheme.colorScheme.onPrimary, fontSize = fontSize )

    }
}

@Composable
fun PrimaryTextButton(
    text: String,
    fontSize: TextUnit,
    onClick: () -> Unit,
    modifier: Modifier =Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            color = MaterialTheme.colorScheme.primary
        )

    }

}



//@Preview(showBackground = true)
//@Composable
//fun ButtonPreview() {
//    DiamondGuestHouseTheme {
//        HomeScreenButton(text = "Add record") {
//
//        }
//    }
//}
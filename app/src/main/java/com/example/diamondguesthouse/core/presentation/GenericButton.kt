package com.example.diamondguesthouse.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GenericButton(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Normal,
    onClick: () -> Unit,
    containerColor: Color = Color(0xFF1E3A5F),
    contentColor: Color = Color.White,
    textSizeSp: Float = 15f,
    icon: @Composable (() -> Unit)? = null,
    shape: RoundedCornerShape = RoundedCornerShape(25.dp),
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = shape,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(10.dp))
            }
            GenericTextView(
                text = text,
                fontWeight = fontWeight,
                textAlign = TextAlign.Center,
                color = contentColor,
            )
        }
    }
}

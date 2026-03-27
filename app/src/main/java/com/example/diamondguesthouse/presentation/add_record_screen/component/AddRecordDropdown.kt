package com.example.diamondguesthouse.presentation.add_record_screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diamondguesthouse.R

@Composable
fun AddRecordDropdown(
    value: String,
    label: String,
    list: List<String>,
    onSelectedChange: (String) -> Unit,
    isAvailable: Boolean = true,
) {
    val expanded = remember { mutableStateOf(false) }
    Box {
        Column {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedTextField(
                value = value,
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded.value = !expanded.value },
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item, fontSize = 13.sp, fontWeight = FontWeight.Medium) },
                    onClick = {
                        onSelectedChange(item)
                        expanded.value = false
                    },
                )
            }
        }
    }
}

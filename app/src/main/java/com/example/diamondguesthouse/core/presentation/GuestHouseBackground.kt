package com.example.diamondguesthouse.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GuestHouseBackground(
    modifier: Modifier = Modifier,
    addScaffolding: Boolean = false,
    snackbarHost: @Composable () -> Unit = { },
    floatingActionButton: @Composable () -> Unit = {},
    surfaceColor: Color = Color(0xFFF5F5F5),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        color = surfaceColor,
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            if (!addScaffolding) {
                content(PaddingValues.Absolute())
            } else {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    floatingActionButton = floatingActionButton,
                    topBar = topBar,
                    snackbarHost = snackbarHost,
                    bottomBar = bottomBar,
                ) { content(it) }
            }
        }
    }
}

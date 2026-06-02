package com.bingoapp.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.bingoapp.util.showToast

@Composable
fun ErrorSnackbar(message: String?, snackbarHostState: SnackbarHostState) {
    val context = LocalContext.current
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            context.showToast(it)
        }
    }
}

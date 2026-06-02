package com.bingoapp.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bingoapp.util.Constants

@Composable
fun UsernameSetupScreen(
    onUsernameSet: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.usernameState
    val snackbarHostState = remember { SnackbarHostState() }
    var username by remember { mutableStateOf("") }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onUsernameSet()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "👤",
                fontSize = 60.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Choose Your Username",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This is how other players will see you",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    if (it.length <= Constants.MAX_USERNAME_LENGTH) {
                        username = it.filter { c -> c.isLetterOrDigit() || c == '_' }
                    }
                },
                label = { Text("Username") },
                placeholder = { Text("cool_player_99") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                trailingIcon = {
                    if (state.isChecking) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else if (state.isAvailable == true) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Available",
                            tint = com.bingoapp.ui.theme.BingoGreen
                        )
                    } else if (state.isAvailable == false) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Not available",
                            tint = com.bingoapp.ui.theme.BingoRed
                        )
                    }
                },
                supportingText = {
                    Text(
                        "${username.length}/${Constants.MAX_USERNAME_LENGTH} characters"
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.saveUsername(username) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = username.length >= Constants.MIN_USERNAME_LENGTH
                        && state.isAvailable == true
                        && !state.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Continue", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

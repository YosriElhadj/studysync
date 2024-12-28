package com.example.studysync.ui.auth.forgot_password

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studysync.ui.auth.components.StudySyncButton
import com.example.studysync.ui.auth.components.StudySyncTextField
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showSuccessMessage by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            showSuccessMessage = true
            delay(2000) // Show success message for 2 seconds
            onNavigateBack()
        }
    }

    // Handle snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error, state.successMessage) {
        when {
            state.error != null -> {
                snackbarHostState.showSnackbar(
                    message = state.error!!,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearError()
            }
            state.successMessage != null -> {
                snackbarHostState.showSnackbar(
                    message = state.successMessage!!,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reset Password") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Enter your email address and we'll send you instructions to reset your password.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            StudySyncTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                isEnabled = !state.isLoading,
                isError = state.error != null,
                supportingText = if (state.error != null) {
                    { Text(state.error!!) }
                } else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                StudySyncButton(
                    text = if (state.isLoading) "Sending..." else "Reset Password",
                    onClick = { viewModel.onResetPasswordClick() },
                    enabled = !state.isLoading && state.email.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp)
                    )
                }
            }

            if (showSuccessMessage) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "Reset instructions sent to your email!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
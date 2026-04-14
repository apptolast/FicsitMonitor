package com.apptolast.fiscsitmonitor.presentation.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LoginError
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LoginEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LoginUiState
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LoginViewModel
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.app_title
import ficsitmonitor.composeapp.generated.resources.auth_login_email
import ficsitmonitor.composeapp.generated.resources.auth_login_error_invalid
import ficsitmonitor.composeapp.generated.resources.auth_login_no_account
import ficsitmonitor.composeapp.generated.resources.auth_login_password
import ficsitmonitor.composeapp.generated.resources.auth_login_submit
import ficsitmonitor.composeapp.generated.resources.auth_login_subtitle
import ficsitmonitor.composeapp.generated.resources.auth_login_title
import ficsitmonitor.composeapp.generated.resources.common_error_generic
import ficsitmonitor.composeapp.generated.resources.common_error_network
import ficsitmonitor.composeapp.generated.resources.common_required
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    onLoggedIn: (hasServer: Boolean) -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.events.collectAsStateWithLifecycle()

    LaunchedEffect(event) {
        when (val current = event) {
            is LoginEvent.LoggedIn -> {
                viewModel.consumeEvent()
                onLoggedIn(current.hasServer)
            }

            null -> Unit
        }
    }

    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::submit,
        onNavigateToRegister = onNavigateToRegister,
    )
}

@Composable
private fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToRegister: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(Res.string.app_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(Res.string.auth_login_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(Res.string.auth_login_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(Res.string.auth_login_email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(Res.string.auth_login_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            )

            state.error?.let { error ->
                val message = when (error) {
                    LoginError.InvalidCredentials -> stringResource(Res.string.auth_login_error_invalid)
                    LoginError.Validation -> stringResource(Res.string.common_required)
                    LoginError.Network -> stringResource(Res.string.common_error_network)
                    LoginError.Generic -> stringResource(Res.string.common_error_generic)
                }
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
                Text(stringResource(Res.string.auth_login_submit))
            }

            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            ) {
                Text(stringResource(Res.string.auth_login_no_account))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    FicsitMonitorTheme {
        LoginContent(
            state = LoginUiState(email = "pioneer@ficsit.com"),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onNavigateToRegister = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLoginScreenError() {
    FicsitMonitorTheme {
        LoginContent(
            state = LoginUiState(
                email = "pioneer@ficsit.com",
                password = "wrong",
                error = LoginError.InvalidCredentials,
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onNavigateToRegister = {},
        )
    }
}

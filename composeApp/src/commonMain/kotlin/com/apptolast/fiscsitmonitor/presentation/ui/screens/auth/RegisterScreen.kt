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
import com.apptolast.fiscsitmonitor.presentation.viewmodel.RegisterError
import com.apptolast.fiscsitmonitor.presentation.viewmodel.RegisterEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.RegisterUiState
import com.apptolast.fiscsitmonitor.presentation.viewmodel.RegisterViewModel
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.app_title
import ficsitmonitor.composeapp.generated.resources.auth_login_email
import ficsitmonitor.composeapp.generated.resources.auth_login_password
import ficsitmonitor.composeapp.generated.resources.auth_register_error_validation
import ficsitmonitor.composeapp.generated.resources.auth_register_have_account
import ficsitmonitor.composeapp.generated.resources.auth_register_name
import ficsitmonitor.composeapp.generated.resources.auth_register_password_confirm
import ficsitmonitor.composeapp.generated.resources.auth_register_submit
import ficsitmonitor.composeapp.generated.resources.auth_register_title
import ficsitmonitor.composeapp.generated.resources.common_error_generic
import ficsitmonitor.composeapp.generated.resources.common_error_network
import ficsitmonitor.composeapp.generated.resources.common_invalid_email
import ficsitmonitor.composeapp.generated.resources.common_password_mismatch
import ficsitmonitor.composeapp.generated.resources.common_password_too_short
import ficsitmonitor.composeapp.generated.resources.common_required
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.events.collectAsStateWithLifecycle()

    LaunchedEffect(event) {
        if (event is RegisterEvent.Registered) {
            viewModel.consumeEvent()
            onRegistered()
        }
    }

    RegisterContent(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordConfirmationChange = viewModel::onPasswordConfirmationChange,
        onSubmit = viewModel::submit,
        onNavigateToLogin = onNavigateToLogin,
    )
}

@Composable
private fun RegisterContent(
    state: RegisterUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmationChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToLogin: () -> Unit,
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
                text = stringResource(Res.string.auth_register_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(Res.string.auth_register_name)) },
                singleLine = true,
                isError = state.fieldErrors.containsKey("name"),
                supportingText = state.fieldErrors["name"]?.let { code -> { FieldError(code) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(Res.string.auth_login_email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = state.fieldErrors.containsKey("email"),
                supportingText = state.fieldErrors["email"]?.let { code -> { FieldError(code) } },
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
                isError = state.fieldErrors.containsKey("password"),
                supportingText = state.fieldErrors["password"]?.let { code -> { FieldError(code) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            )
            OutlinedTextField(
                value = state.passwordConfirmation,
                onValueChange = onPasswordConfirmationChange,
                label = { Text(stringResource(Res.string.auth_register_password_confirm)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = state.fieldErrors.containsKey("password_confirmation"),
                supportingText = state.fieldErrors["password_confirmation"]?.let { code -> { FieldError(code) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            )

            state.error?.let { error ->
                val res: StringResource = when (error) {
                    RegisterError.Validation -> Res.string.auth_register_error_validation
                    RegisterError.Network -> Res.string.common_error_network
                    RegisterError.Generic -> Res.string.common_error_generic
                }
                Text(
                    text = stringResource(res),
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
                Text(stringResource(Res.string.auth_register_submit))
            }

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            ) {
                Text(stringResource(Res.string.auth_register_have_account))
            }
        }
    }
}

@Composable
private fun FieldError(code: String) {
    val message = when (code) {
        "required" -> stringResource(Res.string.common_required)
        "invalid_email" -> stringResource(Res.string.common_invalid_email)
        "too_short" -> stringResource(Res.string.common_password_too_short)
        "mismatch" -> stringResource(Res.string.common_password_mismatch)
        else -> code
    }
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
    )
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    FicsitMonitorTheme {
        RegisterContent(
            state = RegisterUiState(name = "Pioneer", email = "pioneer@ficsit.com"),
            onNameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordConfirmationChange = {},
            onSubmit = {},
            onNavigateToLogin = {},
        )
    }
}

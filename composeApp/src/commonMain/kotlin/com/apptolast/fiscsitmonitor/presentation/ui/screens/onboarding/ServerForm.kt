package com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.common_error_generic
import ficsitmonitor.composeapp.generated.resources.common_error_network
import ficsitmonitor.composeapp.generated.resources.common_required
import ficsitmonitor.composeapp.generated.resources.server_form_api_port
import ficsitmonitor.composeapp.generated.resources.server_form_api_token
import ficsitmonitor.composeapp.generated.resources.server_form_api_token_hint
import ficsitmonitor.composeapp.generated.resources.server_form_error_invalid_port
import ficsitmonitor.composeapp.generated.resources.server_form_frm_http
import ficsitmonitor.composeapp.generated.resources.server_form_frm_ws
import ficsitmonitor.composeapp.generated.resources.server_form_host
import ficsitmonitor.composeapp.generated.resources.server_form_host_hint
import ficsitmonitor.composeapp.generated.resources.server_form_name
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class ServerFormCallbacks(
    val onNameChange: (String) -> Unit,
    val onHostChange: (String) -> Unit,
    val onApiPortChange: (String) -> Unit,
    val onFrmHttpPortChange: (String) -> Unit,
    val onFrmWsPortChange: (String) -> Unit,
    val onApiTokenChange: (String) -> Unit,
    val onSubmit: () -> Unit,
)

@Composable
fun ServerForm(
    state: ServerFormState,
    submitLabel: String,
    callbacks: ServerFormCallbacks,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = callbacks.onNameChange,
            label = { Text(stringResource(Res.string.server_form_name)) },
            singleLine = true,
            isError = state.fieldErrors.containsKey("name"),
            supportingText = state.fieldErrors["name"]?.let { code -> { FieldError(code) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSubmitting,
        )

        OutlinedTextField(
            value = state.host,
            onValueChange = callbacks.onHostChange,
            label = { Text(stringResource(Res.string.server_form_host)) },
            placeholder = { Text(stringResource(Res.string.server_form_host_hint)) },
            singleLine = true,
            isError = state.fieldErrors.containsKey("host"),
            supportingText = state.fieldErrors["host"]?.let { code -> { FieldError(code) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSubmitting,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PortField(
                value = state.apiPort,
                onChange = callbacks.onApiPortChange,
                label = Res.string.server_form_api_port,
                errorCode = state.fieldErrors["api_port"],
                enabled = !state.isSubmitting,
                modifier = Modifier.weight(1f),
            )
            PortField(
                value = state.frmHttpPort,
                onChange = callbacks.onFrmHttpPortChange,
                label = Res.string.server_form_frm_http,
                errorCode = state.fieldErrors["frm_http_port"],
                enabled = !state.isSubmitting,
                modifier = Modifier.weight(1f),
            )
            PortField(
                value = state.frmWsPort,
                onChange = callbacks.onFrmWsPortChange,
                label = Res.string.server_form_frm_ws,
                errorCode = state.fieldErrors["frm_ws_port"],
                enabled = !state.isSubmitting,
                modifier = Modifier.weight(1f),
            )
        }

        OutlinedTextField(
            value = state.apiToken,
            onValueChange = callbacks.onApiTokenChange,
            label = { Text(stringResource(Res.string.server_form_api_token)) },
            placeholder = { Text(stringResource(Res.string.server_form_api_token_hint)) },
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isSubmitting,
        )

        state.error?.let { error ->
            val message: String = when (error) {
                ServerFormError.Validation -> stringResource(Res.string.common_required)
                ServerFormError.Network -> stringResource(Res.string.common_error_network)
                ServerFormError.Generic -> stringResource(Res.string.common_error_generic)
            }
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Button(
            onClick = callbacks.onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !state.isSubmitting,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 8.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            Text(
                text = submitLabel,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun PortField(
    value: String,
    onChange: (String) -> Unit,
    label: StringResource,
    errorCode: String?,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> onChange(input.filter { it.isDigit() }.take(5)) },
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = errorCode != null,
        supportingText = errorCode?.let { code -> { FieldError(code) } },
        modifier = modifier,
        enabled = enabled,
    )
}

@Composable
private fun FieldError(code: String) {
    val message = when (code) {
        "required" -> stringResource(Res.string.common_required)
        "invalid_port" -> stringResource(Res.string.server_form_error_invalid_port)
        else -> code
    }
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
    )
}

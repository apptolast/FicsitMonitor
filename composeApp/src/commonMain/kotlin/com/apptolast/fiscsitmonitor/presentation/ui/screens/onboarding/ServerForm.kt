package com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.common_error_generic
import ficsitmonitor.composeapp.generated.resources.common_error_network
import ficsitmonitor.composeapp.generated.resources.common_required
import ficsitmonitor.composeapp.generated.resources.server_form_admin_password
import ficsitmonitor.composeapp.generated.resources.server_form_admin_password_hint
import ficsitmonitor.composeapp.generated.resources.server_form_advanced_transport_hint
import ficsitmonitor.composeapp.generated.resources.server_form_advanced_transport_title
import ficsitmonitor.composeapp.generated.resources.server_form_api_port
import ficsitmonitor.composeapp.generated.resources.server_form_connecting
import ficsitmonitor.composeapp.generated.resources.server_form_error_admin_password_invalid
import ficsitmonitor.composeapp.generated.resources.server_form_error_admin_password_too_short
import ficsitmonitor.composeapp.generated.resources.server_form_error_invalid_port
import ficsitmonitor.composeapp.generated.resources.server_form_error_path_prefix_leading_slash
import ficsitmonitor.composeapp.generated.resources.server_form_error_provisioning_failed
import ficsitmonitor.composeapp.generated.resources.server_form_error_server_unreachable
import ficsitmonitor.composeapp.generated.resources.server_form_frm_http
import ficsitmonitor.composeapp.generated.resources.server_form_frm_ws
import ficsitmonitor.composeapp.generated.resources.server_form_host
import ficsitmonitor.composeapp.generated.resources.server_form_host_hint
import ficsitmonitor.composeapp.generated.resources.server_form_name
import ficsitmonitor.composeapp.generated.resources.server_form_path_prefix_label
import ficsitmonitor.composeapp.generated.resources.server_form_scheme_label
import ficsitmonitor.composeapp.generated.resources.server_form_verify_tls_hint
import ficsitmonitor.composeapp.generated.resources.server_form_verify_tls_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class ServerFormCallbacks(
    val onNameChange: (String) -> Unit,
    val onHostChange: (String) -> Unit,
    val onApiPortChange: (String) -> Unit,
    val onFrmHttpPortChange: (String) -> Unit,
    val onFrmWsPortChange: (String) -> Unit,
    val onSchemeChange: (String) -> Unit,
    val onPathPrefixChange: (String) -> Unit,
    val onVerifyTlsChange: (Boolean) -> Unit,
    val onAdvancedExpandedChange: (Boolean) -> Unit,
    val onAdminPasswordChange: (String) -> Unit,
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

        AdvancedTransportSection(
            state = state,
            callbacks = callbacks,
        )

        if (state.requireAdminPassword) {
            AdminPasswordField(
                value = state.adminPassword,
                onChange = callbacks.onAdminPasswordChange,
                errorCode = state.fieldErrors["admin_password"],
                enabled = !state.isSubmitting,
            )
        }

        state.error?.let { error ->
            val message: String = when (error) {
                ServerFormError.Validation -> stringResource(Res.string.common_required)
                ServerFormError.Network -> stringResource(Res.string.common_error_network)
                ServerFormError.Unreachable -> stringResource(Res.string.server_form_error_server_unreachable)
                ServerFormError.ProvisioningFailed -> stringResource(Res.string.server_form_error_provisioning_failed)
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
            val label = if (state.isSubmitting && state.requireAdminPassword) {
                stringResource(Res.string.server_form_connecting)
            } else {
                submitLabel
            }
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun AdvancedTransportSection(
    state: ServerFormState,
    callbacks: ServerFormCallbacks,
) {
    val rotation by animateFloatAsState(if (state.advancedExpanded) 180f else 0f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !state.isSubmitting) {
                callbacks.onAdvancedExpandedChange(!state.advancedExpanded)
            }
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.server_form_advanced_transport_title),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.rotate(rotation),
            )
        }
    }

    AnimatedVisibility(visible = state.advancedExpanded) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(Res.string.server_form_advanced_transport_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(Res.string.server_form_scheme_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SchemeChip(
                        label = "https",
                        selected = state.scheme == "https",
                        enabled = !state.isSubmitting,
                        onSelect = { callbacks.onSchemeChange("https") },
                    )
                    SchemeChip(
                        label = "http",
                        selected = state.scheme == "http",
                        enabled = !state.isSubmitting,
                        onSelect = { callbacks.onSchemeChange("http") },
                    )
                }
            }

            OutlinedTextField(
                value = state.pathPrefix,
                onValueChange = callbacks.onPathPrefixChange,
                label = { Text(stringResource(Res.string.server_form_path_prefix_label)) },
                singleLine = true,
                isError = state.fieldErrors.containsKey("path_prefix"),
                supportingText = state.fieldErrors["path_prefix"]?.let { code -> { FieldError(code) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(Res.string.server_form_verify_tls_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = stringResource(Res.string.server_form_verify_tls_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = state.verifyTls,
                    onCheckedChange = callbacks.onVerifyTlsChange,
                    enabled = !state.isSubmitting,
                )
            }
        }
    }
}

@Composable
private fun SchemeChip(
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onSelect: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onSelect,
        label = { Text(label) },
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
    )
}

@Composable
private fun AdminPasswordField(
    value: String,
    onChange: (String) -> Unit,
    errorCode: String?,
    enabled: Boolean,
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(stringResource(Res.string.server_form_admin_password)) },
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = errorCode != null,
        supportingText = {
            if (errorCode != null) {
                FieldError(errorCode)
            } else {
                Text(
                    text = stringResource(Res.string.server_form_admin_password_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null,
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
    )
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
        "too_short" -> stringResource(Res.string.server_form_error_admin_password_too_short)
        "provisioning_failed" -> stringResource(Res.string.server_form_error_admin_password_invalid)
        "path_prefix_leading_slash" -> stringResource(Res.string.server_form_error_path_prefix_leading_slash)
        else -> code
    }
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
    )
}

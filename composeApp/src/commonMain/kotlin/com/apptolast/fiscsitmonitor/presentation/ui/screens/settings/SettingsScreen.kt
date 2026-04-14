package com.apptolast.fiscsitmonitor.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.domain.model.UserServer
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerForm
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormCallbacks
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormState
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SettingsEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SettingsUiState
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SettingsViewModel
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.common_error_generic
import ficsitmonitor.composeapp.generated.resources.server_form_submit_edit
import ficsitmonitor.composeapp.generated.resources.server_form_title_edit
import ficsitmonitor.composeapp.generated.resources.settings_advanced_section
import ficsitmonitor.composeapp.generated.resources.settings_base_url
import ficsitmonitor.composeapp.generated.resources.settings_base_url_hint
import ficsitmonitor.composeapp.generated.resources.settings_logout
import ficsitmonitor.composeapp.generated.resources.settings_saved
import ficsitmonitor.composeapp.generated.resources.settings_select_server
import ficsitmonitor.composeapp.generated.resources.settings_server_section
import ficsitmonitor.composeapp.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.events.collectAsStateWithLifecycle()

    LaunchedEffect(event) {
        if (event is SettingsEvent.LoggedOut) {
            viewModel.consumeEvent()
            onLoggedOut()
        }
    }

    SettingsContent(
        state = state,
        onBack = onBack,
        onNameChange = viewModel::onNameChange,
        onHostChange = viewModel::onHostChange,
        onApiPortChange = viewModel::onApiPortChange,
        onFrmHttpPortChange = viewModel::onFrmHttpPortChange,
        onFrmWsPortChange = viewModel::onFrmWsPortChange,
        onApiTokenChange = viewModel::onApiTokenChange,
        onSubmit = viewModel::saveServer,
        onBaseUrlChange = viewModel::onBaseUrlChange,
        onLogout = viewModel::logout,
        onSelectServer = viewModel::onSelectServer,
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onNameChange: (String) -> Unit,
    onHostChange: (String) -> Unit,
    onApiPortChange: (String) -> Unit,
    onFrmHttpPortChange: (String) -> Unit,
    onFrmWsPortChange: (String) -> Unit,
    onApiTokenChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBaseUrlChange: (String) -> Unit,
    onLogout: () -> Unit,
    onSelectServer: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            onBack = onBack,
            title = stringResource(Res.string.settings_title),
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            return@Column
        }

        state.loadError?.let {
            Text(
                text = it.ifBlank { stringResource(Res.string.common_error_generic) },
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (state.servers.size > 1) {
            ServerPicker(
                servers = state.servers,
                selectedId = state.selectedServerId,
                onSelect = onSelectServer,
            )
        }

        Text(
            text = stringResource(Res.string.settings_server_section),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(Res.string.server_form_title_edit),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        ServerForm(
            state = state.form,
            submitLabel = stringResource(Res.string.server_form_submit_edit),
            callbacks = ServerFormCallbacks(
                onNameChange = onNameChange,
                onHostChange = onHostChange,
                onApiPortChange = onApiPortChange,
                onFrmHttpPortChange = onFrmHttpPortChange,
                onFrmWsPortChange = onFrmWsPortChange,
                onApiTokenChange = onApiTokenChange,
                onSubmit = onSubmit,
            ),
        )

        if (state.isSaved) {
            Text(
                text = stringResource(Res.string.settings_saved),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.settings_advanced_section),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        OutlinedTextField(
            value = state.baseUrlOverride,
            onValueChange = onBaseUrlChange,
            label = { Text(stringResource(Res.string.settings_base_url)) },
            supportingText = { Text(stringResource(Res.string.settings_base_url_hint)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
        ) {
            Text(stringResource(Res.string.settings_logout))
        }
    }
}

@Composable
private fun Row(onBack: () -> Unit, title: String) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ServerPicker(
    servers: List<UserServer>,
    selectedId: Int?,
    onSelect: (Int) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val current = servers.firstOrNull { it.id == selectedId }
    Column {
        Text(
            text = stringResource(Res.string.settings_select_server),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = current?.name ?: "",
                modifier = Modifier.weight(1f),
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            servers.forEach { server ->
                DropdownMenuItem(
                    text = { Text(server.name) },
                    onClick = {
                        expanded = false
                        onSelect(server.id)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    FicsitMonitorTheme {
        SettingsContent(
            state = SettingsUiState(
                isLoading = false,
                servers = listOf(
                    UserServer(
                        id = 1,
                        name = "My Factory",
                        host = "46.224.182.211",
                        apiPort = 7777,
                        frmHttpPort = 8080,
                        frmWsPort = 8081,
                        apiToken = null,
                        status = "online",
                        lastSeenAt = null,
                        createdAt = null,
                    ),
                ),
                selectedServerId = 1,
                form = ServerFormState(
                    name = "My Factory",
                    host = "46.224.182.211",
                ),
            ),
            onBack = {},
            onNameChange = {},
            onHostChange = {},
            onApiPortChange = {},
            onFrmHttpPortChange = {},
            onFrmWsPortChange = {},
            onApiTokenChange = {},
            onSubmit = {},
            onBaseUrlChange = {},
            onLogout = {},
            onSelectServer = {},
        )
    }
}

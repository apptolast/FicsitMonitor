package com.apptolast.fiscsitmonitor.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.apptolast.fiscsitmonitor.presentation.ui.components.NativeAd
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerForm
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormCallbacks
import com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding.ServerFormState
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SettingsEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SettingsUiState
import com.apptolast.fiscsitmonitor.presentation.viewmodel.SettingsViewModel
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.common_error_generic
import ficsitmonitor.composeapp.generated.resources.language_name_en
import ficsitmonitor.composeapp.generated.resources.language_name_es
import ficsitmonitor.composeapp.generated.resources.language_name_nl
import ficsitmonitor.composeapp.generated.resources.language_name_de
import ficsitmonitor.composeapp.generated.resources.server_form_submit_edit
import ficsitmonitor.composeapp.generated.resources.server_form_title_edit
import ficsitmonitor.composeapp.generated.resources.settings_language_section
import ficsitmonitor.composeapp.generated.resources.settings_logout
import ficsitmonitor.composeapp.generated.resources.settings_saved
import ficsitmonitor.composeapp.generated.resources.settings_select_server
import ficsitmonitor.composeapp.generated.resources.settings_server_section
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
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
        onNameChange = viewModel::onNameChange,
        onHostChange = viewModel::onHostChange,
        onApiPortChange = viewModel::onApiPortChange,
        onFrmHttpPortChange = viewModel::onFrmHttpPortChange,
        onFrmWsPortChange = viewModel::onFrmWsPortChange,
        onSubmit = viewModel::saveServer,
        onLogout = viewModel::logout,
        onSelectServer = viewModel::onSelectServer,
        onLocaleSelected = viewModel::onLocaleSelected,
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onNameChange: (String) -> Unit = {},
    onHostChange: (String) -> Unit = {},
    onApiPortChange: (String) -> Unit = {},
    onFrmHttpPortChange: (String) -> Unit = {},
    onFrmWsPortChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {},
    onLogout: () -> Unit = {},
    onSelectServer: (Int) -> Unit = {},
    onLocaleSelected: (String) -> Unit = {},
) {
    // No Scaffold / TopAppBar here on purpose — the title "Ajustes" and back button are
    // rendered by the root FicsitNavHost Scaffold's topBar slot, which already consumes the
    // system insets. This Column receives the correctly-sized inner padding from that root
    // Scaffold via the parent NavHost.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

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
                    onAdminPasswordChange = {},
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

            LocaleSection(
                current = state.currentLocale,
                available = state.availableLocales,
                onSelect = onLocaleSelected,
            )

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) {
                Text(stringResource(Res.string.settings_logout))
            }

        NativeAd()
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

@Composable
private fun LocaleSection(
    current: String?,
    available: List<String>,
    onSelect: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(Res.string.settings_language_section),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            available.forEach { tag ->
                FilterChip(
                    selected = tag == current,
                    onClick = { onSelect(tag) },
                    label = { Text(localeDisplayName(tag)) },
                )
            }
        }
    }
}

@Composable
private fun localeDisplayName(tag: String): String = when (tag) {
    "en" -> stringResource(Res.string.language_name_en)
    "es" -> stringResource(Res.string.language_name_es)
    "nl" -> stringResource(Res.string.language_name_nl)
    "de" -> stringResource(Res.string.language_name_de)
    else -> tag
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
        )
    }
}

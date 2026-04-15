package com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.AddServerEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.AddServerViewModel
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.app_title
import ficsitmonitor.composeapp.generated.resources.server_form_submit_add
import ficsitmonitor.composeapp.generated.resources.server_form_subtitle
import ficsitmonitor.composeapp.generated.resources.server_form_title_add
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddServerScreen(
    onServerCreated: () -> Unit,
    viewModel: AddServerViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val event by viewModel.events.collectAsStateWithLifecycle()

    LaunchedEffect(event) {
        if (event is AddServerEvent.Created) {
            viewModel.consumeEvent()
            onServerCreated()
        }
    }

    AddServerContent(
        state = state,
        callbacks = ServerFormCallbacks(
            onNameChange = viewModel::onNameChange,
            onHostChange = viewModel::onHostChange,
            onApiPortChange = viewModel::onApiPortChange,
            onFrmHttpPortChange = viewModel::onFrmHttpPortChange,
            onFrmWsPortChange = viewModel::onFrmWsPortChange,
            onAdminPasswordChange = viewModel::onAdminPasswordChange,
            onSubmit = viewModel::submit,
        ),
    )
}

@Composable
private fun AddServerContent(
    state: ServerFormState,
    callbacks: ServerFormCallbacks,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(Res.string.app_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(Res.string.server_form_title_add),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = stringResource(Res.string.server_form_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        ServerForm(
            state = state,
            submitLabel = stringResource(Res.string.server_form_submit_add),
            callbacks = callbacks,
        )
    }
}

@Preview
@Composable
private fun PreviewAddServerScreen() {
    FicsitMonitorTheme {
        AddServerContent(
            state = ServerFormState(requireAdminPassword = true),
            callbacks = ServerFormCallbacks(
                onNameChange = {},
                onHostChange = {},
                onApiPortChange = {},
                onFrmHttpPortChange = {},
                onFrmWsPortChange = {},
                onAdminPasswordChange = {},
                onSubmit = {},
            ),
        )
    }
}

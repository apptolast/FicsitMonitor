package com.apptolast.fiscsitmonitor.presentation.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.ui.theme.FicsitMonitorTheme
import com.apptolast.fiscsitmonitor.presentation.viewmodel.AddServerEvent
import com.apptolast.fiscsitmonitor.presentation.viewmodel.AddServerViewModel
import ficsitmonitor.composeapp.generated.resources.Res
import ficsitmonitor.composeapp.generated.resources.app_title
import ficsitmonitor.composeapp.generated.resources.help_field_admin_password
import ficsitmonitor.composeapp.generated.resources.help_field_api_port
import ficsitmonitor.composeapp.generated.resources.help_field_frm_http
import ficsitmonitor.composeapp.generated.resources.help_field_frm_ws
import ficsitmonitor.composeapp.generated.resources.help_field_host
import ficsitmonitor.composeapp.generated.resources.help_field_name
import ficsitmonitor.composeapp.generated.resources.help_sheet_docs_link
import ficsitmonitor.composeapp.generated.resources.help_sheet_fields_title
import ficsitmonitor.composeapp.generated.resources.help_sheet_intro_p1
import ficsitmonitor.composeapp.generated.resources.help_sheet_intro_p2
import ficsitmonitor.composeapp.generated.resources.help_sheet_intro_p3
import ficsitmonitor.composeapp.generated.resources.help_sheet_intro_p4
import ficsitmonitor.composeapp.generated.resources.help_sheet_intro_title
import ficsitmonitor.composeapp.generated.resources.help_sheet_title
import ficsitmonitor.composeapp.generated.resources.server_form_admin_password
import ficsitmonitor.composeapp.generated.resources.server_form_api_port
import ficsitmonitor.composeapp.generated.resources.server_form_frm_http
import ficsitmonitor.composeapp.generated.resources.server_form_frm_ws
import ficsitmonitor.composeapp.generated.resources.server_form_host
import ficsitmonitor.composeapp.generated.resources.server_form_name
import ficsitmonitor.composeapp.generated.resources.server_form_submit_add
import ficsitmonitor.composeapp.generated.resources.server_form_subtitle
import ficsitmonitor.composeapp.generated.resources.server_form_title_add
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

private const val DOCS_URL = "https://apptolast.github.io/FicsitDocumentation/"

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddServerContent(
    state: ServerFormState,
    callbacks: ServerFormCallbacks,
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Expanded,
            skipHiddenState = true,
        ),
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 72.dp,
        sheetContent = { AddServerHelpSheet() },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
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
}

@Composable
private fun AddServerHelpSheet() {
    val uriHandler = LocalUriHandler.current

    Text(
        text = stringResource(Res.string.help_sheet_title),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp),
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
    ) {
        Text(
            text = stringResource(Res.string.help_sheet_fields_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        HelpFieldItem(
            label = stringResource(Res.string.server_form_name),
            description = stringResource(Res.string.help_field_name),
        )
        HelpFieldItem(
            label = stringResource(Res.string.server_form_host),
            description = stringResource(Res.string.help_field_host),
        )
        HelpFieldItem(
            label = stringResource(Res.string.server_form_api_port),
            description = stringResource(Res.string.help_field_api_port),
        )
        HelpFieldItem(
            label = stringResource(Res.string.server_form_frm_http),
            description = stringResource(Res.string.help_field_frm_http),
        )
        HelpFieldItem(
            label = stringResource(Res.string.server_form_frm_ws),
            description = stringResource(Res.string.help_field_frm_ws),
        )
        HelpFieldItem(
            label = stringResource(Res.string.server_form_admin_password),
            description = stringResource(Res.string.help_field_admin_password),
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = stringResource(Res.string.help_sheet_intro_title),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp),
        )

        listOf(
            stringResource(Res.string.help_sheet_intro_p1),
            stringResource(Res.string.help_sheet_intro_p2),
            stringResource(Res.string.help_sheet_intro_p3),
            stringResource(Res.string.help_sheet_intro_p4),
        ).forEach { paragraph ->
            Text(
                text = paragraph,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 10.dp),
            )
        }

        TextButton(
            onClick = { uriHandler.openUri(DOCS_URL) },
            modifier = Modifier.padding(top = 4.dp),
        ) {
            Text(
                text = stringResource(Res.string.help_sheet_docs_link) + " →",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun HelpFieldItem(
    label: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = buildAnnotatedString {
                append("• ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(label)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

package com.apptolast.fiscsitmonitor.presentation.ui.screens.live

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.apptolast.fiscsitmonitor.presentation.viewmodel.LiveViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LiveScreen(
    viewModel: LiveViewModel = koinViewModel(),
) {
    val events by viewModel.events.collectAsStateWithLifecycle()
    val summary by viewModel.summary.collectAsStateWithLifecycle()

    LiveContent(
        events = events,
        summary = summary,
    )
}

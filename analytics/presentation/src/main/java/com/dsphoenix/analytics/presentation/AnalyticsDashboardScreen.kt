@file:OptIn(ExperimentalMaterial3Api::class)

package com.dsphoenix.analytics.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dsphoenix.analytics.presentation.components.AnalyticsCard
import com.dsphoenix.core.presentation.designsystem.RuniqueTheme
import com.dsphoenix.core.presentation.designsystem.components.RuniqueScaffold
import com.dsphoenix.core.presentation.designsystem.components.RuniqueToolbar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsDashboardScreenRoot(
    onBackClick: () -> Unit,
    viewModel: AnalyticsViewModel = koinViewModel()
) {
    AnalyticsDashboardScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is AnalyticsAction.OnBackClick -> onBackClick()
            }
        }
    )
}

@Composable
private fun AnalyticsDashboardScreen(
    state: AnalyticsDashboardState?,
    onAction: (AnalyticsAction) -> Unit
) {
    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.analytics),
                onBackClick = { onAction(AnalyticsAction.OnBackClick) }
            )
        }
    ) { padding ->
        if (state == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.total_distance),
                        value = state.totalDistance,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title = stringResource(id = R.string.total_time),
                        value = state.totalTime,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.avg_pace),
                        value = state.avgPace,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title = stringResource(id = R.string.avg_distance),
                        value = state.avgDistance,
                        modifier = Modifier.weight(1f)
                    )
                }
                AnalyticsCard(
                    title = stringResource(id = R.string.fastest_run),
                    value = state.maxSpeed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    RuniqueTheme {
        AnalyticsDashboardScreen(
            state = AnalyticsDashboardState(
                totalDistance = "1.2 km",
                totalTime = "0d 8h 20m",
                maxSpeed = "123 km/h",
                avgDistance = "30 km",
                avgPace = "7:30"
            ),
            onAction = {}
        )
    }
}

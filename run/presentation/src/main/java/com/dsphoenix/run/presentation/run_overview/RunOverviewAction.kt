package com.dsphoenix.run.presentation.run_overview

import com.dsphoenix.run.presentation.run_overview.model.RunUi

sealed interface RunOverviewAction {
    data object OnStartClick: RunOverviewAction
    data object OnLogoutClick: RunOverviewAction
    data object OnAnalyticsClick: RunOverviewAction
    data class DeleteRun(val run: RunUi): RunOverviewAction
}

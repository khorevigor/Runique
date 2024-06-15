package com.dsphoenix.run.presentation.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RunOverviewViewModule: ViewModel() {

    var state by mutableStateOf(RunOverviewState())
        private set

    fun onAction(action: RunOverviewAction) {

    }
}

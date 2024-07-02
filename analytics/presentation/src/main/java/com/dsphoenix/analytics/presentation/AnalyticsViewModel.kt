package com.dsphoenix.analytics.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AnalyticsViewModel: ViewModel() {

    var state by mutableStateOf<AnalyticsDashboardState?>(null)
        private set
}

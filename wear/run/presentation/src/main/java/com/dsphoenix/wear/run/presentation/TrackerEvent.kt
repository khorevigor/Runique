package com.dsphoenix.wear.run.presentation

import com.dsphoenix.presentation.ui.UiText

sealed interface TrackerEvent {
    data object RunFinished: TrackerEvent
    data class Error(val message: UiText): TrackerEvent
}

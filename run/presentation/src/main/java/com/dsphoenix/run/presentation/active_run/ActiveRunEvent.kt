package com.dsphoenix.run.presentation.active_run

import com.dsphoenix.presentation.ui.UiText

sealed interface ActiveRunEvent {
    data class Error(val error: UiText): ActiveRunEvent
    data object RunSaved: ActiveRunEvent
}

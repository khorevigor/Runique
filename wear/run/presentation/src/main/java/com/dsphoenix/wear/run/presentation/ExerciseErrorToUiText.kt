package com.dsphoenix.wear.run.presentation

import com.dsphoenix.presentation.ui.UiText
import com.dsphoenix.wear.run.domain.ExerciseError

fun ExerciseError.toUiText(): UiText? {
    return when (this) {
        ExerciseError.ONGOING_OWN_EXERCISE, // fallthru
        ExerciseError.ONGOING_OTHER_EXERCISE -> UiText.StringResource(R.string.error_ongoing_exercise)
        ExerciseError.TRACKING_NOT_SUPPORTED -> null
        ExerciseError.EXERCISE_ALREADY_ENDED -> UiText.StringResource(R.string.error_exercise_already_ended)
        ExerciseError.UNKNOWN -> UiText.StringResource(com.dsphoenix.presentation.ui.R.string.error_unknown_error)
    }
}

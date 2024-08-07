@file:Suppress("OPT_IN_USAGE")

package com.dsphoenix.wear.run.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsphoenix.core.connectivity.domain.messaging.MessagingAction
import com.dsphoenix.core.domain.util.Result
import com.dsphoenix.core.notification.ActiveRunService
import com.dsphoenix.wear.run.domain.ExerciseTracker
import com.dsphoenix.wear.run.domain.PhoneConnector
import com.dsphoenix.wear.run.domain.RunningTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TrackerViewModel(
    private val exerciseTracker: ExerciseTracker,
    private val phoneConnector: PhoneConnector,
    private val runningTracker: RunningTracker
) : ViewModel() {

    var state by mutableStateOf(
        TrackerState(
            hasStartedRunning = ActiveRunService.isServiceActive.value,
            isRunActive = ActiveRunService.isServiceActive.value && runningTracker.isTrackingActive.value,
            isTrackable = ActiveRunService.isServiceActive.value
        )
    )
        private set

    private val hasBodySensorPermission = MutableStateFlow(false)

    private val isTrackingActive = snapshotFlow {
        state.isRunActive && state.isTrackable && state.isConnectedPhoneNearby
    }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            false
        )

    private val eventChannel = Channel<TrackerEvent>()
    val events = eventChannel.receiveAsFlow()

    private val isAmbientMode = snapshotFlow { state.isAmbientMode }

    init {
        phoneConnector
            .connectedNode
            .filterNotNull()
            .onEach { node ->
                state = state.copy(isConnectedPhoneNearby = node.isNearby)
            }
            .combine(isTrackingActive) { _, isTrackingActive ->
                if (!isTrackingActive) {
                    phoneConnector.sendToPhone(MessagingAction.ConnectionRequest)
                }
            }
            .launchIn(viewModelScope)

        runningTracker
            .isTrackable
            .onEach { isTrackable ->
                state = state.copy(isTrackable = isTrackable)
            }
            .launchIn(viewModelScope)

        isTrackingActive
            .onEach { isTracking ->
                val result = when {
                    isTracking && !state.hasStartedRunning -> {
                        exerciseTracker.startExercise()
                    }

                    isTracking && state.hasStartedRunning -> {
                        exerciseTracker.resumeExercise()
                    }

                    !isTracking && state.hasStartedRunning -> {
                        exerciseTracker.pauseExercise()
                    }

                    else -> Result.Success(Unit)
                }

                if (result is Result.Error) {
                    result.error.toUiText()?.let {
                        eventChannel.send(TrackerEvent.Error(it))
                    }
                }

                if (isTracking) {
                    state = state.copy(hasStartedRunning = true)
                }
                runningTracker.setIsTrackingActive(isTracking)
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val isHeartRateTrackingSupported = exerciseTracker.isHeartRateTrackingSupported()
            state = state.copy(canTrackHeartRate = isHeartRateTrackingSupported)
        }

        runningTracker
            .heartRate
            .throttleIfAmbient()
            .onEach {
                state = state.copy(heartRate = it)
            }.launchIn(viewModelScope)

        runningTracker
            .distanceMeters
            .throttleIfAmbient()
            .onEach {
                state = state.copy(distanceMeters = it)
            }
            .launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .throttleIfAmbient()
            .onEach {
                state = state.copy(elapsedDuration = it)
            }
            .launchIn(viewModelScope)

        listenToPhoneActions()
    }

    fun onAction(action: TrackerAction, triggeredOnPhone: Boolean = false) {
        if (!triggeredOnPhone) {
            sendToPhone(action)
        }

        when (action) {
            TrackerAction.OnFinishRunClick -> {
                viewModelScope.launch {
                    exerciseTracker.stopExercise()
                    eventChannel.send(TrackerEvent.RunFinished)

                    state = state.copy(
                        elapsedDuration = Duration.ZERO,
                        distanceMeters = 0,
                        heartRate = 0,
                        hasStartedRunning = false,
                        isRunActive = false
                    )
                }
            }

            TrackerAction.OnToggleRunClick -> {
                if (state.isTrackable) {
                    state = state.copy(
                        isRunActive = !state.isRunActive
                    )
                }
            }

            is TrackerAction.OnBodySensorPermissionResult -> {
                hasBodySensorPermission.value = action.isGranted
                if (action.isGranted) {
                    viewModelScope.launch {
                        val isHeartRateTrackingSupported =
                            exerciseTracker.isHeartRateTrackingSupported()
                        state = state.copy(canTrackHeartRate = isHeartRateTrackingSupported)
                    }
                }
            }

            is TrackerAction.OnEnterAmbientMode -> {
                state = state.copy(
                    isAmbientMode = true,
                    burnInProtectionRequired = action.burnInProtectionRequired
                )
            }

            TrackerAction.OnExitAmbientMode -> {
                state = state.copy(
                    isAmbientMode = false,
                    burnInProtectionRequired = false
                )
            }
        }
    }

    private fun sendToPhone(action: TrackerAction) {
        viewModelScope.launch {
            val messagingAction = when (action) {
                TrackerAction.OnFinishRunClick -> MessagingAction.Finish
                TrackerAction.OnToggleRunClick -> {
                    if (state.isRunActive) {
                        MessagingAction.Pause
                    } else {
                        MessagingAction.StartOrResume
                    }
                }

                else -> null
            }

            messagingAction?.let {
                phoneConnector.sendToPhone(it)
            }
        }
    }

    private fun listenToPhoneActions() {
        phoneConnector
            .messagingActions
            .onEach { action ->
                when (action) {
                    MessagingAction.Finish -> {
                        onAction(TrackerAction.OnFinishRunClick, triggeredOnPhone = true)
                    }

                    MessagingAction.Pause -> {
                        if (state.isTrackable) {
                            state = state.copy(isRunActive = false)
                        }
                    }

                    MessagingAction.StartOrResume -> {
                        if (state.isTrackable) {
                            state = state.copy(isRunActive = true)
                        }
                    }

                    MessagingAction.Trackable -> {
                        state = state.copy(isTrackable = true)
                    }

                    MessagingAction.Untrackable -> {
                        state = state.copy(isTrackable = false)
                    }

                    else -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    private fun <T> Flow<T>.throttleIfAmbient(samplePeriod: Duration = 10.seconds): Flow<T> {
        return isAmbientMode.flatMapLatest { isAmbient ->
            if (isAmbient) {
                this.sample(samplePeriod)
            } else {
                this
            }
        }
    }
}

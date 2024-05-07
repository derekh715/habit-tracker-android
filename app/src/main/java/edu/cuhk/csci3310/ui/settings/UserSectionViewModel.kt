package edu.cuhk.csci3310.ui.settings

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.di.DataStoreManager
import edu.cuhk.csci3310.di.KeyDefaultValue
import edu.cuhk.csci3310.di.Settings
import edu.cuhk.csci3310.notifications.DelayedNotificationWorker
import edu.cuhk.csci3310.ui.formUtils.TextInputInfo
import edu.cuhk.csci3310.ui.formUtils.ToggleableInfo
import edu.cuhk.csci3310.ui.utils.CommonUiEvent
import edu.cuhk.csci3310.ui.utils.UiEvent
import edu.cuhk.csci3310.ui.utils.mutableStateIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class UserSectionViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    application: Application
) : AndroidViewModel(application) {
    private val _uiChannel = Channel<UiEvent>()
    val uiChannel = _uiChannel.receiveAsFlow()

    val postNotification =
        dataStoreManager.postNotifications.map {
            ToggleableInfo(
                text = "Enable Notifications",
                toggled = it,

                )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ToggleableInfo(text = "Enable Notifications", toggled = false)
            )

    val showDebugOptions =
        dataStoreManager.showDebugOptions.map {
            ToggleableInfo(
                text = "Show Debug Options",
                toggled = it,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = ToggleableInfo(text = "Show Debug Options", toggled = false)
            )

    val notifyAt = dataStoreManager.notifyAt.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LocalTime.of(
            Settings.NOTIFY_AT_HOURS.second,
            Settings.NOTIFY_AT_MINUTES.second
        )
    )

    private fun showRecordsUntilOptions(value: Int = 1): TextInputInfo {
        return TextInputInfo(
            label = "Show records for months:",
            isNumberInput = true,
            placeholder = "",
            value = value.toString(),
            icon = Icons.Filled.CalendarMonth
        )
    }

    private val _showRecordsUntil = dataStoreManager.showRecordsUntil.map {
        showRecordsUntilOptions(it)
    }.mutableStateIn(
        scope = viewModelScope,
        initialValue = showRecordsUntilOptions()
    )
    val showRecordsUntil = _showRecordsUntil.asStateFlow()

    private fun schedule(time: LocalTime) {
        val request = DelayedNotificationWorker.buildWorkerRequest(
            DelayedNotificationWorker.calculateDelay(time)
        )
        WorkManager.getInstance(getApplication<Application>().applicationContext)
            .enqueueUniqueWork(
                DelayedNotificationWorker.WORK_TAG,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }

    fun changeRecordsUntil(newRecordsUntil: String) {
        viewModelScope.launch {
            try {
                val parsed = newRecordsUntil.toInt()
                if (parsed <= 0) {
                    _showRecordsUntil.emit(
                        _showRecordsUntil.value.copy(
                            errorMessage = "Value must be a positive integer",
                            showError = true,
                            value = newRecordsUntil
                        )
                    )
                    return@launch
                }
                _showRecordsUntil.emit(
                    _showRecordsUntil.value.copy(
                        errorMessage = "",
                        showError = false,
                        value = newRecordsUntil
                    )
                )
                setValue(Settings.SHOW_RECORDS_UNTIL, parsed)
            } catch (error: NumberFormatException) {
                _showRecordsUntil.emit(
                    _showRecordsUntil.value.copy(
                        errorMessage = "The month count must be an integer.",
                        showError = true,
                        value = newRecordsUntil
                    )
                )
            }
        }
    }


    fun changeNotifyAt(time: LocalTime) {
        viewModelScope.launch {
            if (!postNotification.value.toggled) {
                return@launch
            }
            setValue(Settings.NOTIFY_AT_HOURS, time.hour)
            setValue(Settings.NOTIFY_AT_MINUTES, time.minute)
            schedule(time)
            sendEvent(CommonUiEvent.ShowToast("Next notification will be at $time"))
        }
    }

    fun toggleNotification() {
        val isEnabledNextTime = !postNotification.value.toggled
        setValue(
            Settings.POST_NOTIFICATIONS,
            isEnabledNextTime
        )
        if (!isEnabledNextTime) {
            viewModelScope.launch {
                WorkManager.getInstance(getApplication<Application>().applicationContext)
                    .cancelAllWorkByTag(DelayedNotificationWorker.WORK_TAG)
            }
            sendEvent(CommonUiEvent.ShowToast("No more notifications will be shown"))
        } else {
            // reschedule the request since the timer is on
            schedule(notifyAt.value)
            sendEvent(CommonUiEvent.ShowToast("Next notification will be at ${notifyAt.value}"))
        }
    }

    fun showDebugOptions() {
        setValue(
            Settings.SHOW_DEBUG_OPTIONS,
            !showDebugOptions.value.toggled
        )
    }

    fun <T> setValue(key: KeyDefaultValue<T>, value: T) {
        viewModelScope.launch {
            dataStoreManager.setValue(key, value)
        }
    }

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiChannel.send(event)
        }
    }

}
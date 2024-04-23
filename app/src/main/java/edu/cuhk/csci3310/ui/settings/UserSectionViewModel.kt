package edu.cuhk.csci3310.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.di.DataStoreManager
import edu.cuhk.csci3310.di.KeyDefaultValue
import edu.cuhk.csci3310.di.Settings
import edu.cuhk.csci3310.notifications.DelayedNotificationWorker
import edu.cuhk.csci3310.ui.formUtils.ToggleableInfo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class UserSectionViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val habitDao: HabitDao,
    application: Application
) : AndroidViewModel(application) {
    val postNotification =
        dataStoreManager.postNotifications.map {
            ToggleableInfo(
                text = "Enable Notifications",
                toggled = it,

                )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
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
                started = SharingStarted.Eagerly,
                initialValue = ToggleableInfo(text = "Show Debug Options", toggled = false)
            )

    val notifyAt = dataStoreManager.notifyAt.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LocalTime.of(
            Settings.NOTIFY_AT_HOURS.second,
            Settings.NOTIFY_AT_MINUTES.second
        )
    )


    fun changeNotifyAt(time: LocalTime) {
        viewModelScope.launch {
            setValue(Settings.NOTIFY_AT_HOURS, time.hour)
            setValue(Settings.NOTIFY_AT_MINUTES, time.minute)
            val request = DelayedNotificationWorker.buildWorkerRequest(
                habitDao.getTitlesOfPendingHabits(),
                DelayedNotificationWorker.calculateDelay(time)
            )
            WorkManager.getInstance(getApplication<Application>().applicationContext)
                .enqueueUniqueWork(
                    DelayedNotificationWorker.WORK_TAG,
                    ExistingWorkPolicy.REPLACE,
                    request
                )
        }
    }

    fun <T> setValue(key: KeyDefaultValue<T>, value: T) {
        viewModelScope.launch {
            dataStoreManager.setValue(key, value)
        }
    }

}
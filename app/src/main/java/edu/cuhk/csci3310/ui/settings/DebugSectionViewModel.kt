package edu.cuhk.csci3310.ui.settings

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.FrequencyUnit
import edu.cuhk.csci3310.data.Group
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.HabitDao
import edu.cuhk.csci3310.data.Record
import edu.cuhk.csci3310.data.RecordStatus
import edu.cuhk.csci3310.di.DataStoreManager
import edu.cuhk.csci3310.notifications.DailyNotificationService
import edu.cuhk.csci3310.notifications.DelayedNotificationWorker
import edu.cuhk.csci3310.ui.formUtils.TextInputInfo
import edu.cuhk.csci3310.ui.utils.Calculations.calculateNextDay
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

enum class TextInputEnum {
    Habit,
    Group,
    Record,
}

@HiltViewModel
class DebugSectionViewModel
@Inject
constructor(
    private val habitDao: HabitDao,
    private val groupDao: GroupDao,
    private val dataStoreManager: DataStoreManager,
    application: Application,
) : AndroidViewModel(application) {
    private var faker: Faker? = null
    private val now: LocalDate = LocalDate.now()
    private val rand = Random.Default
    private val service = DailyNotificationService(application)

    val showDebugOptions = dataStoreManager.showDebugOptions.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = false
    )

    private val _amount =
        MutableStateFlow(
            TextInputInfo(
                isNumberInput = true,
                label = "Amount",
            ),
        )
    val amount = _amount.asStateFlow()

    fun valueChanged(
        value: String,
    ) {
        viewModelScope.launch {
            _amount.emit(
                _amount.value.copy(
                    value = value,
                ),
            )
        }
    }

    fun addHabits() {
        viewModelScope.launch {
            try {
                if (faker == null) {
                    faker = Faker()
                }
                repeat(_amount.value.value.toInt()) {
                    val freq =
                        Frequency(
                            times = rand.nextInt(from = 1, until = 10),
                            unit = FrequencyUnit.MONTHLY,
                        )
                    habitDao.insertHabit(
                        Habit(
                            description = faker!!.lorem.words(),
                            title = faker!!.lorem.words(),
                            frequency = freq,
                            positive = listOf(false, true).random(),
                            until = generateRandomDate(),
                            nextTime = calculateNextDay(LocalDate.now(), freq),
                        ),
                    )
                }
            } catch (_: NumberFormatException) {
            }
        }
    }

    fun addGroups() {
        viewModelScope.launch {
            try {
                if (faker == null) {
                    faker = Faker()
                }
                repeat(_amount.value.value.toInt()) {
                    groupDao.insertGroup(
                        Group(
                            description = faker!!.lorem.words(),
                            name = faker!!.lorem.words(),
                            colour = Color(rand.nextLong()).toArgb(),
                        ),
                    )
                }
            } catch (_: NumberFormatException) {
            }
        }
    }

    fun addRecords() {
        viewModelScope.launch {
            val habitIds = habitDao.getAllHabitIds()
            try {
                repeat(_amount.value.value.toInt()) {
                    habitDao.insertRecord(
                        Record(
                            status =
                            when (rand.nextInt(from = 1, until = 4)) {
                                1 -> RecordStatus.FULFILLED
                                2 -> RecordStatus.UNFULFILLED
                                3 -> RecordStatus.SKIPPED
                                else -> RecordStatus.NOTFILLED
                            },
                            date = generateRandomDate(),
                            habitId = habitIds.random(),
                            recordId = null,
                            reason = null,
                            times = 1
                        ),
                    )
                }
            } catch (_: NumberFormatException) {
            }
        }
    }

    fun removeAllHabits() {
        viewModelScope.launch {
            habitDao.deleteAllHabits()
        }
    }

    fun removeAllGroups() {
        viewModelScope.launch {
            groupDao.deleteAllGroups()
        }
    }

    fun removeAllRecord() {
        viewModelScope.launch {
            habitDao.deleteAllRecords()
        }
    }

    fun showTestNotification() {
        service.showNotification(listOf("Test Habit"))
    }

    fun showDailyNotification() {
        viewModelScope.launch {
            val names = habitDao.getTitlesOfPendingHabits()
            service.showNotification(names)
        }
    }

    fun scheduleNotification() {
        viewModelScope.launch {
            // hardcoded 10 seconds of delay for testing
            val request = DelayedNotificationWorker.buildWorkerRequest(10)
            WorkManager.getInstance(getApplication<Application>().applicationContext)
                .enqueueUniqueWork(
                    DelayedNotificationWorker.WORK_TAG,
                    ExistingWorkPolicy.REPLACE,
                    request
                )
        }
    }

    private fun generateRandomDate(): LocalDate {
        return LocalDate.of(
            now.year,
            rand.nextInt(from = now.month.value, until = 12),
            rand.nextInt(1, 31),
        )
    }
}

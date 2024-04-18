package edu.cuhk.csci3310.ui.debug

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
import edu.cuhk.csci3310.notifications.DailyNotificationService
import edu.cuhk.csci3310.notifications.DelayedNotificationWorker
import edu.cuhk.csci3310.ui.formUtils.TextInputInfo
import edu.cuhk.csci3310.ui.utils.Calculations.calculateNextDay
import io.github.serpro69.kfaker.Faker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

enum class TextInputEnum {
    Habit,
    Group,
    Record,
}

@HiltViewModel
class DebugScreenViewModel
    @Inject
    constructor(
        private val habitDao: HabitDao,
        private val groupDao: GroupDao,
        application: Application,
    ) : AndroidViewModel(application) {
        private val faker = Faker()
        private val now: LocalDate = LocalDate.now()
        private val rand = Random.Default
        private val service = DailyNotificationService(application)

        private val _habitAmount =
            MutableStateFlow(
                TextInputInfo(
                    isNumberInput = true,
                    label = "Habit Amount",
                ),
            )
        val habitAmount = _habitAmount.asStateFlow()
        private val _groupAmount =
            MutableStateFlow(
                TextInputInfo(
                    isNumberInput = true,
                    label = "Group Amount",
                ),
            )
        val groupAmount = _groupAmount.asStateFlow()
        private val _recordAmount =
            MutableStateFlow(
                TextInputInfo(
                    isNumberInput = true,
                    label = "Record Amount",
                ),
            )
        val recordAmount = _recordAmount.asStateFlow()

        fun valueChanged(
            value: String,
            which: TextInputEnum,
        ) {
            viewModelScope.launch {
                when (which) {
                    TextInputEnum.Habit -> {
                        _habitAmount.emit(
                            _habitAmount.value.copy(
                                value = value,
                            ),
                        )
                    }
                    TextInputEnum.Group -> {
                        _groupAmount.emit(
                            _groupAmount.value.copy(
                                value = value,
                            ),
                        )
                    }
                    TextInputEnum.Record -> {
                        _recordAmount.emit(
                            _recordAmount.value.copy(
                                value = value,
                            ),
                        )
                    }
                }
            }
        }

        fun addHabits() {
            viewModelScope.launch {
                try {
                    repeat(_habitAmount.value.value.toInt()) {
                        val freq =
                            Frequency(
                                times = rand.nextInt(from = 1, until = 10),
                                unit = FrequencyUnit.MONTHLY,
                            )
                        habitDao.insertHabit(
                            Habit(
                                description = faker.lorem.words(),
                                title = faker.lorem.words(),
                                frequency = freq,
                                positive = listOf(false, true).random(),
                                until = generateRandomDate(),
                                nextTime = calculateNextDay(freq),
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
                    repeat(_groupAmount.value.value.toInt()) {
                        groupDao.insertGroup(
                            Group(
                                description = faker.lorem.words(),
                                name = faker.lorem.words(),
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
                    repeat(_recordAmount.value.value.toInt()) {
                        habitDao.addRecord(
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
                val names = habitDao.getTitlesOfPendingHabits()
                // hardcoded 10 seconds of delay for testing
                val request = DelayedNotificationWorker.buildWorkerRequest(names, 10)
                WorkManager.getInstance(getApplication<Application>().applicationContext)
                    .enqueueUniqueWork(DelayedNotificationWorker.WORK_TAG, ExistingWorkPolicy.REPLACE, request)
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

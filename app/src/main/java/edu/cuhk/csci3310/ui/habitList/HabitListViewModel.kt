package edu.cuhk.csci3310.ui.habitList

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.FrequencyUnit
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.HabitDao
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel
    @Inject
    constructor(
        private val habitDao: HabitDao,
        application: Application,
    ) : AndroidViewModel(application) {
        private val context = getApplication<Application>().applicationContext

        fun onEvent(event: HabitListEvent) {
            when (event) {
                is HabitListEvent.AddHabit -> {
                    insertHabit(event.habit)
                }
                is HabitListEvent.ChangeHabit -> {
                }
                is HabitListEvent.AddDummyHabit -> {
                    insertTodoTest()
                }
                is HabitListEvent.RemoveHabit -> {
                    removeHabit(event.habit)
                }
            }
        }

        val habitsList = habitDao.getHabitsOnly()
        private var lastDeletedHabit: Habit? = null
        private val logCategory = "HabitListViewModel"

        private fun insertHabit(habit: Habit) {
            viewModelScope.launch {
                habitDao.insertHabit(habit)
                Toast.makeText(context, "Habit inserted", Toast.LENGTH_LONG).show()
            }
        }

        private fun insertTodoTest() {
            viewModelScope.launch {
                habitDao.insertHabit(
                    Habit(
                        title = "My Habit",
                        description = "Okay",
                        positive = true,
                        until = Date(),
                        frequency =
                            Frequency(
                                unit = FrequencyUnit.DAILY,
                                times = 5,
                            ),
                    ),
                )
                Log.i(logCategory, "dummy habit added")
            }
        }

        private fun removeHabit(habit: Habit) {
            viewModelScope.launch {
                lastDeletedHabit = habit
                habitDao.deleteHabit(habit)
                Log.i(logCategory, "deleted a habit")
            }
        }
    }

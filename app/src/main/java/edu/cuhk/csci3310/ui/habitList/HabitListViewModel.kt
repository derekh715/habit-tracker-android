package edu.cuhk.csci3310.ui.habitList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.Habit
import edu.cuhk.csci3310.data.HabitDao
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitListViewModel
    @Inject
    constructor(
        private val habitDao: HabitDao,
    ) : ViewModel() {
        fun insertTodoTest() {
            viewModelScope.launch {
                habitDao.insertHabit(
                    Habit(
                        title = "My Habit",
                        description = "Okay",
                        positive = true,
                        until = 1333918,
                        frequency =
                            Frequency(
                                unit = "Day",
                                times = 5,
                            ),
                    ),
                )
            }
        }
    }

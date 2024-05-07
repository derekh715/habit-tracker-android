package edu.cuhk.csci3310.ui.utils

import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.FrequencyUnit
import java.time.LocalDate

/*
Calculates the next day if the user changes the completion status to completed
Calculates the prev day if the user resets the completion status to not filled
 */
object Calculations {

    fun calculateNextDay(base: LocalDate = LocalDate.now(), freq: Frequency): LocalDate {
        return when (freq.unit) {
            FrequencyUnit.DAILY -> base.plusDays(1)
            FrequencyUnit.WEEKLY -> base.plusDays(7L / freq.times)
            FrequencyUnit.MONTHLY -> base.plusDays(base.lengthOfMonth().toLong() / freq.times)
            FrequencyUnit.YEARLY -> base.plusDays(base.lengthOfYear().toLong() / freq.times)
        }
    }

    fun calculatePreviousDay(base: LocalDate = LocalDate.now(), freq: Frequency): LocalDate {
        return when (freq.unit) {
            FrequencyUnit.DAILY -> base.minusDays(1)
            FrequencyUnit.WEEKLY -> base.minusDays(7L / freq.times)
            FrequencyUnit.MONTHLY -> base.minusDays(base.lengthOfMonth().toLong() / freq.times)
            FrequencyUnit.YEARLY -> base.minusDays(base.lengthOfYear().toLong() / freq.times)
        }
    }
}

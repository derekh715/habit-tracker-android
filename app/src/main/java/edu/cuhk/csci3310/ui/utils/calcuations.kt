package edu.cuhk.csci3310.ui.utils

import edu.cuhk.csci3310.data.Frequency
import edu.cuhk.csci3310.data.FrequencyUnit
import java.time.LocalDate

object Calculations {
    fun calculateNextDay(freq: Frequency): LocalDate {
        val base = LocalDate.now()
        return when (freq.unit) {
            FrequencyUnit.DAILY -> base.plusDays(1)
            FrequencyUnit.WEEKLY -> base.plusDays(7L / freq.times)
            FrequencyUnit.MONTHLY -> base.plusDays(base.lengthOfMonth().toLong() / freq.times)
            FrequencyUnit.YEARLY -> base.plusDays(base.lengthOfYear().toLong() / freq.times)
        }
    }
}

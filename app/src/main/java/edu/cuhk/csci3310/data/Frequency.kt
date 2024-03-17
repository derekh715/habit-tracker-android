package edu.cuhk.csci3310.data

enum class FrequencyUnit {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
}

data class Frequency(
    // e.g. three times per month, four times per week
    val unit: FrequencyUnit,
    // check higher than zero times
    // for daily times can only be one
    val times: Int,
)

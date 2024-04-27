package edu.cuhk.csci3310.ui.utils

import java.time.LocalDate

class DateIterator(
    private val startDate: LocalDate,
    private val endDateInclusive: LocalDate,
    private val stepDays: Long
) : Iterator<LocalDate> {

    private var currentDate = startDate

    override fun hasNext() = currentDate.plusDays(stepDays) <= endDateInclusive

    override fun next(): LocalDate {
        val next = currentDate
        currentDate = currentDate.plusDays(stepDays)
        return next
    }
}

class DateProgression(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    private val stepDays: Long = 1
) : Iterable<LocalDate>,
    ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> =
        DateIterator(start, endInclusive, stepDays)

    infix fun step(days: Long) = DateProgression(start, endInclusive, days)

}

operator fun LocalDate.rangeTo(other: LocalDate) = DateProgression(this, other)
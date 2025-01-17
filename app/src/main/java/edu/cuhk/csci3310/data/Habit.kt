package edu.cuhk.csci3310.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "habit",
)
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val habitId: Long? = null,
    // if the habit is positive (wants to achieve)
    // or negative (wants to avoid)
    val positive: Boolean,
    // when does the habit end?
    val until: LocalDate,
    val title: String,
    val description: String?,
    @Embedded
    val frequency: Frequency,
    val nextTime: LocalDate,
    @ColumnInfo(defaultValue = "0")
    var creationDate: LocalDate = LocalDate.now(),
)

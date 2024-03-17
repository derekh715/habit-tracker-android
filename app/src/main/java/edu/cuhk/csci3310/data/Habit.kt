package edu.cuhk.csci3310.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

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
    val until: Date,
    val title: String,
    val description: String?,
    @Embedded
    val frequency: Frequency,
)

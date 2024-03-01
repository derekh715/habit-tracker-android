package edu.cuhk.csci3310.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "habit_group_cross_ref",
    primaryKeys = ["habitId", "groupId"],
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["habitId"],
            childColumns = ["habitId"]
        ),
        ForeignKey(
            entity = Group::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"]
        )
    ]
)
data class HabitGroupCrossRef(
    val habitId: Long,
    val groupId: Long
)

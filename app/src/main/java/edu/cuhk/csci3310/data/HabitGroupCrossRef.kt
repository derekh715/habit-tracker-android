package edu.cuhk.csci3310.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "habit_group_cross_ref",
    primaryKeys = ["habitId", "groupId"],
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["habitId"],
            childColumns = ["habitId"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Group::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = CASCADE
        )
    ]
)
data class HabitGroupCrossRef(
    val habitId: Long,
    val groupId: Long
)

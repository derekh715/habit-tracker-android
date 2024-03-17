package edu.cuhk.csci3310.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class RecordStatus {
    FULFILLED,
    UNFULFILLED,
    SKIPPED,
}

@Entity(
    tableName = "record",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = arrayOf("habitId"),
            childColumns = arrayOf("habitId"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class Record(
    @PrimaryKey(autoGenerate = true)
    val recordId: Long,
    // foreign key: which habit is this record about?
    val habitId: Long,
    val status: RecordStatus,
    // skipped reason (if status is not skipped, ignore this field)
    val reason: String?,
)

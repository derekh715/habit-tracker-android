package edu.cuhk.csci3310.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert

@Dao
interface HabitDao {
    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

//    @Query("")
//    suspend fun getHabits(): List<Habit>

//    @Query("SELECT * FROM record WHERE habitId = :habitId")
//    suspend fun getRecordsOfHabit(habitId: Long): List<Record>

    @Upsert
    suspend fun addRecord(record: Record)
}
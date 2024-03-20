package edu.cuhk.csci3310.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class GroupListOption(
    @Embedded
    val group: Group,
    var selected: Boolean,
)

@Dao
interface HabitDao {
    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit WHERE habitId = :habitId")
    suspend fun getHabitById(habitId: Long): Habit

    @Query("SELECT * FROM habit")
    fun getHabitsOnly(): Flow<List<Habit>>

    @Query("SELECT * FROM record WHERE habitId = :habitId AND date >= :notBefore ORDER BY date DESC")
    suspend fun getRecordsOfHabit(
        habitId: Long,
        notBefore: LocalDate,
    ): List<Record>

    // long returns the record id
    @Upsert
    suspend fun addRecord(record: Record): Long

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query(
        "SELECT g.* FROM habit_group_cross_ref hg INNER JOIN `group` g ON g.groupId = hg.groupId " +
            "INNER JOIN habit h ON h.habitId = hg.habitId WHERE hg.habitId = :habitId",
    )
    fun getGroupsOfHabit(habitId: Long): Flow<List<Group>>

    @Query(
        "SELECT g.*, (SELECT COUNT(*)" +
            " FROM habit_group_cross_ref hg WHERE g.groupId = hg.groupId AND :habitId = hg.habitId)" +
            " as selected FROM `group` g",
    )
    suspend fun getAllGroupsWithIsInGroupOrNot(habitId: Long): List<GroupListOption>

    @Query("SELECT habitId FROM habit")
    suspend fun getAllHabitIds(): List<Long>

    @Query("DELETE FROM habit")
    suspend fun deleteAllHabits()

    @Query("DELETE FROM record")
    suspend fun deleteAllRecords()
}

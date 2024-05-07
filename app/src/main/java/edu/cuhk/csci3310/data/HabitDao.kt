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

data class GroupColour(
    val groupName: String,
    val groupColour: Int,
)

@Dao
interface HabitDao {
    @Upsert
    suspend fun insertHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habit WHERE habitId = :habitId")
    fun getHabitById(habitId: Long): Flow<Habit?>

    @Query("SELECT * FROM habit")
    fun getHabitsOnly(): Flow<List<Habit>>

    @Query("SELECT * FROM record WHERE habitId = :habitId AND date >= :notBefore ORDER BY date DESC")
    fun getRecordsOfHabit(
        habitId: Long,
        notBefore: LocalDate,
    ): Flow<List<Record>>

    // long returns the record id
    @Upsert
    suspend fun insertRecord(record: Record): Long

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query(
        "SELECT g.* FROM habit_group_cross_ref hg INNER JOIN `group` g ON g.groupId = hg.groupId " +
                "INNER JOIN habit h ON h.habitId = hg.habitId WHERE hg.habitId = :habitId",
    )
    fun getGroupsOfHabit(habitId: Long): Flow<List<Group>>

    @Query(
        """
        SELECT h.*, g.name AS groupName, g.colour AS groupColour FROM habit_group_cross_ref hg 
        INNER JOIN habit h ON hg.habitId = h.habitId 
        INNER JOIN `group` g ON hg.groupId = g.groupId
        UNION SELECT h.*, 'No Group' as groupName, 1 as groupColour FROM habit h
        WHERE h.habitId NOT IN (SELECT habitId FROM habit_group_cross_ref)
        """
    )
    fun getAllHabitsWithGroups(): Flow<
            Map<
                    GroupColour,
                    List<Habit>,
                    >,
            >

    @Query(
        "SELECT g.*, (SELECT COUNT(*)" +
                " FROM habit_group_cross_ref hg WHERE g.groupId = hg.groupId AND :habitId = hg.habitId)" +
                " as selected FROM `group` g",
    )
    fun getAllGroupsWithIsInGroupOrNot(habitId: Long): Flow<List<GroupListOption>>

    // this is for the debug section
    // no need to convert to state
    @Query("SELECT habitId FROM habit")
    suspend fun getAllHabitIds(): List<Long>

    @Query("SELECT title FROM habit WHERE nextTime <= :time")
    suspend fun getTitlesOfPendingHabits(time: LocalDate = LocalDate.now()): List<String>

    @Query("DELETE FROM habit")
    suspend fun deleteAllHabits()

    @Query("DELETE FROM record")
    suspend fun deleteAllRecords()
}

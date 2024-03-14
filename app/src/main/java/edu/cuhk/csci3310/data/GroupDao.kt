package edu.cuhk.csci3310.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Upsert
    suspend fun insertGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @Query("SELECT * FROM `group`")
    fun getGroups(): Flow<List<Group>>

//    @Query("")
//    suspend fun addHabitIntoGroup(
//        group: Group,
//        habit: Habit,
//    )

//    @Query("")
//    suspend fun removeHabitFromGroup(
//        group: Group,
//        habit: Habit,
//    )

//    @Query("")
//    suspend fun getHabitsOfGroup(groupId: Long): List<Habit>
}

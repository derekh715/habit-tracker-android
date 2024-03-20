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

    @Upsert
    suspend fun addHabitIntoGroup(pair: HabitGroupCrossRef)

    @Delete
    suspend fun removeHabitFromGroup(pair: HabitGroupCrossRef)

    @Query("DELETE FROM `group`")
    suspend fun deleteAllGroups()
}

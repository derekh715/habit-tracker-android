package edu.cuhk.csci3310.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

data class GroupNameAndId(
    val groupId: Long,
    val name: String,
)

@Dao
interface GroupDao {
    @Upsert
    suspend fun insertGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)

    @Query("SELECT * FROM `group`")
    fun getGroups(): Flow<List<Group>>

    @Query("SELECT groupId, name FROM `group`")
    suspend fun getGroupNamesAndIds(): List<GroupNameAndId>

    @Upsert
    suspend fun addHabitIntoGroup(pair: HabitGroupCrossRef)

//    @Query("")
//    suspend fun removeHabitFromGroup(
//        group: Group,
//        habit: Habit,
//    )

//    @Query("")
//    suspend fun getHabitsOfGroup(groupId: Long): List<Habit>
}

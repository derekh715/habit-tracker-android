package edu.cuhk.csci3310.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Group::class, Habit::class, Record::class, HabitGroupCrossRef::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
    abstract val groupDao: GroupDao
}

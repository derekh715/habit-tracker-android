package edu.cuhk.csci3310.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Group::class, Habit::class, Record::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao
    abstract val groupDao: GroupDao
}

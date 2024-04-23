package edu.cuhk.csci3310.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.cuhk.csci3310.data.AppDatabase
import edu.cuhk.csci3310.data.GroupDao
import edu.cuhk.csci3310.data.HabitDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "habit_tracker_db").build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(db: AppDatabase): HabitDao {
        return db.habitDao
    }

    @Provides
    @Singleton
    fun provideGroupDao(db: AppDatabase): GroupDao {
        return db.groupDao
    }

    @Provides
    @Singleton
    fun dataStoreManager(@ApplicationContext appContext: Context): DataStoreManager =
        DataStoreManager(appContext)
}

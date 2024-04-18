package edu.cuhk.csci3310

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import edu.cuhk.csci3310.notifications.DailyNotificationService

@HiltAndroidApp
class HabitTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationService()
    }

    private fun createNotificationService() {
        // versions below Oreo does not have notification channels
        // but our version is above Oreo
        val channel =
            NotificationChannel(
                DailyNotificationService.CHANNEL_ID,
                "Daily Reminder",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        channel.description = "Reminds the user to complete the habits"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}

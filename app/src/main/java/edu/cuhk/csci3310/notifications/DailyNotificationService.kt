package edu.cuhk.csci3310.notifications

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import edu.cuhk.csci3310.MainActivity
import edu.cuhk.csci3310.R

class DailyNotificationService(
    private val context: Context,
) {
    companion object {
        const val CHANNEL_ID = "daily"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(habitNames: List<String>) {
        val notification = buildNotification(habitNames)
        notificationManager.notify(1, notification)
    }

    private fun buildNotification(habitNames: List<String>): Notification {
        val intent = Intent(context, MainActivity::class.java)
        val activityPendingIntent =
            PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)
        val contentText = if (habitNames.isEmpty()) {
            "Today's habits are completed"
        } else {
            "There are ${habitNames.size} habits to be completed before tomorrow." +
                    "\nThey are: ${habitNames.joinToString(", ")}"
        }
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Daily Reminder")
            .setContentText(
                contentText
            )
            .setContentIntent(activityPendingIntent)
            .build()
    }
}

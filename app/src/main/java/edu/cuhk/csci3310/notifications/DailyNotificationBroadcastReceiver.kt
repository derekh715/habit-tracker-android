package edu.cuhk.csci3310.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import edu.cuhk.csci3310.di.DataStoreManager
import kotlinx.coroutines.flow.first

class DailyNotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (context == null || intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            return@goAsync
        }
        val dt = DataStoreManager(context)
        val time = dt.notifyAt.first()
        val delay = DelayedNotificationWorker.calculateDelay(time)
        val request = DelayedNotificationWorker.buildWorkerRequest(delay)
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                DelayedNotificationWorker.WORK_TAG,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }
}

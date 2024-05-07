package edu.cuhk.csci3310.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import edu.cuhk.csci3310.data.AppDatabase
import edu.cuhk.csci3310.di.DataStoreManager
import kotlinx.coroutines.flow.first
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class DelayedNotificationWorker(
    private val appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(
    appContext,
    workerParameters,
) {
    companion object {
        const val WORK_TAG = "daily_reminder"

        fun calculateDelay(time: LocalTime = LocalTime.of(16, 18)): Long {
            val d = Duration.between(LocalTime.now(), time)
            return if (d.isNegative) {
                Duration.between(
                    // for example, if the time now is 11:00 and the notification time
                    // is set to be 10:00, then the user will wait for 23 hours before the next
                    // notification. 11:00 - 24:00 is 13 hours + 00:00 - 10:00 is 10 hours = 23 hours
                    LocalTime.now(),
                    // midnight gives zero seconds, so I compared time with
                    // 23:59 then add one back
                    LocalTime.MIDNIGHT.minusSeconds(1)
                ).seconds + 1 + Duration.between(
                    LocalTime.MIDNIGHT,
                    time
                ).seconds
            } else {
                d.seconds
            }
        }

        fun buildWorkerRequest(
            delay: Long,
        ): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<DelayedNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .build()
        }
    }

    override suspend fun doWork(): Result {
        val names = AppDatabase.getAppDatabase(appContext)!!.habitDao.getTitlesOfPendingHabits()
        DailyNotificationService(appContext).showNotification(names)
        // we want the notifications to be recurring, so we schedule it again
        reschedule()
        return Result.success()
    }

    private suspend fun reschedule() {
        val dt = DataStoreManager(appContext)
        val time = dt.notifyAt.first()
        val delay = calculateDelay(time)
        val request = buildWorkerRequest(delay)
        WorkManager.getInstance(appContext)
            .enqueueUniqueWork(
                WORK_TAG,
                ExistingWorkPolicy.REPLACE,
                request
            )
    }
}

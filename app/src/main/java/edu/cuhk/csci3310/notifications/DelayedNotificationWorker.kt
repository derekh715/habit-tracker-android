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
                // midnight gives zero seconds
                Duration.between(
                    LocalTime.now(),
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

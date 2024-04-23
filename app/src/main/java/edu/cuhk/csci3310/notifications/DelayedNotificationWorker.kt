package edu.cuhk.csci3310.notifications

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class DelayedNotificationWorker(
    private val appContext: Context,
    workerParameters: WorkerParameters
) : Worker(
    appContext,
    workerParameters,
) {
    companion object {
        const val NAMES_KEY = "names"
        const val WORK_TAG = "daily_reminder"

        fun calculateDelay(time: LocalTime = LocalTime.of(16, 18)): Long {
            val d = Duration.between(LocalTime.now(), time)
            return if (d.isNegative) {
                0
            } else {
                d.seconds
            }
        }

        fun buildWorkerRequest(
            names: List<String>,
            delay: Long,
        ): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<DelayedNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.SECONDS)
                .setInputData(
                    workDataOf(
                        NAMES_KEY to names.toTypedArray(),
                    ),
                )
                .build()
        }
    }

    override fun doWork(): Result {
        val names: List<String> = inputData.getStringArray(NAMES_KEY)!!.asList()
        DailyNotificationService(appContext).showNotification(names)
        return Result.success()
    }
}

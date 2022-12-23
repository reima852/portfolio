package com.example.grademp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.grademp.repositories.MPRepo
import com.example.grademp.room.getMPDatabase
import retrofit2.HttpException
import timber.log.Timber

class MPDataRefreshWorker (appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.grademp.worker.MPDataRefreshWorker"
    }

    //Database refresh from the network
    override suspend fun doWork(): Result {

        val database =  getMPDatabase(applicationContext)
        val repository = MPRepo(database)

        try {
            repository.refreshMPs()
            Timber.d("Work request for sync is run")
        }   catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
}
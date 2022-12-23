package com.example.grademp

import android.app.Application
import androidx.work.*
import com.example.grademp.worker.MPDataRefreshWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * Initializes the app, setting up the worker to update MP's offline database.
 * If in debug mode, also plants the Timber logger.
 */

class AppClass : Application() {


    override fun onCreate() {
        super.onCreate()
        initialization()
    }

    //defining a coroutine scope
    private val appScope = CoroutineScope(Dispatchers.Default)

    //Init function
    private fun initialization() {
        appScope.launch {
            //We don't want to debug all the time and thus,
            //the Timber logger should start only if in debug mode.
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
            //Start the worker which updates the MP-Database
            setUpRepeatingMPUpdate()
        }
    }

    //Setup function for the worker which updates the MP-database from the network every 4 hours.
    private fun setUpRepeatingMPUpdate() {
        //We don't want to do any sync if the battery is low
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        //Make it repeat every 4 hours and add the constraints to it
        val repeatingRequest =
            PeriodicWorkRequestBuilder<MPDataRefreshWorker>(4, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        //Give the work to WorkManager
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            MPDataRefreshWorker.WORK_NAME,
            //If there is a pending work with the same name it won't queue more
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }




}
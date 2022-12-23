package com.example.grademp.repositories

import androidx.lifecycle.LiveData
import com.example.grademp.networking.MetropoliaNetworkFetcher
import com.example.grademp.networking.toDatabaseMP
import com.example.grademp.room.MPBase
import com.example.grademp.room.DatabaseMemberOfFinnishParliament
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * The repository which contains data from the MP -database.
 */

class MPRepo (private val database: MPBase) {

    //Live data from the offline MP-database
    val members: LiveData<List<DatabaseMemberOfFinnishParliament>> = database.mPDao.getAllMPsAsLiveDataList()

    //For updating the MP-database
    suspend fun refreshMPs() {
        withContext(Dispatchers.IO) {
            Timber.d("Refreshing MP database")

            try {
                //Fetch mps from network
                val mPList = MetropoliaNetworkFetcher.service.getMPs()
                //Insert them into mp database whilst preserving all existing status data
                val currentMembers = database.mPDao.getAllMPsAsList()
                database.mPDao.insertAll(toDatabaseMP(mPList, currentMembers))
            }catch (e: Exception) {Timber.e(e)}

        }
    }
}
package com.example.grademp.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * This contains the MPBase Dao and MPBase initialization call
 */

@Dao
interface MPDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll (memberOfFinnishParliamentList: List<DatabaseMemberOfFinnishParliament>)

    @Update
    suspend fun update (memberOfFinnishParliament: DatabaseMemberOfFinnishParliament)

    @Query("SELECT * from member_of_finnish_parliament_table WHERE personNum = :key")
    suspend fun get (key: Long): DatabaseMemberOfFinnishParliament?

    @Query("SELECT * FROM member_of_finnish_parliament_table ORDER BY personNum DESC")
    fun getAllMPsAsLiveDataList(): LiveData<List<DatabaseMemberOfFinnishParliament>>

    @Query("SELECT * FROM member_of_finnish_parliament_table ORDER BY personNum DESC")
    fun getAllMPsAsList(): List<DatabaseMemberOfFinnishParliament>
}

@Database(entities = [DatabaseMemberOfFinnishParliament::class], version = 1, exportSchema = false)
abstract class MPBase: RoomDatabase() {
    abstract val mPDao: MPDatabaseDao
}

private lateinit var INSTANCE: MPBase

fun getMPDatabase(context: Context): MPBase {
    synchronized(MPBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MPBase::class.java,
                "mp_db"
            ).build()
        }
    }
    return INSTANCE
}


package com.example.grademp.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @author Reima N.
 * @since 3.3.2022
 *
 * This file contains the NoteDatabase Dao and NoteDatabase initialization call
 */

@Dao
interface NoteDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote (mPNote: MPNote)

    @Delete
    suspend fun deleteNote (mPNote: MPNote)

    @Update
    suspend fun updateNote (mPNote: MPNote)

    @Query("SELECT * from finnish_mp_notes WHERE person_identifier = :key")
    suspend fun get (key: Int): MPNote

    @Query("SELECT * FROM finnish_mp_notes ORDER BY person_identifier DESC")
    fun getAllNotes(): LiveData<List<MPNote>>
}

@Database(entities = [MPNote::class], version = 1, exportSchema = false)
abstract class NoteBase: RoomDatabase() {
    abstract val noteDatabaseDao: NoteDatabaseDao

}

private lateinit var INSTANCE: NoteBase

fun getNoteDatabase(context: Context): NoteBase {
    synchronized(NoteBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NoteBase::class.java,
                "note_db"
            ).build()
        }
    }
    return INSTANCE
}
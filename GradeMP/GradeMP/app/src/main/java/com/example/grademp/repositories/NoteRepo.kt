package com.example.grademp.repositories

import androidx.lifecycle.LiveData
import com.example.grademp.room.MPNote
import com.example.grademp.room.NoteBase

/**
 * @author Reima N.
 * @since 3.3.2022
 *
 * The repository which contains data from the MPNotes -database.
 */

class NoteRepo (database: NoteBase){

    //Live data from the MPNote-database
    val notes: LiveData<List<MPNote>> = database.noteDatabaseDao.getAllNotes()

}
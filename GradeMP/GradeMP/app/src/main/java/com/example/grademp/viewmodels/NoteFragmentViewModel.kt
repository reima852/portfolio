package com.example.grademp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.grademp.repositories.MPRepo
import com.example.grademp.repositories.NoteRepo
import com.example.grademp.room.DatabaseMemberOfFinnishParliament
import com.example.grademp.room.MPNote
import com.example.grademp.room.getMPDatabase
import com.example.grademp.room.getNoteDatabase
import timber.log.Timber
import java.lang.Exception
import java.time.LocalDateTime

/**
 * @author Reima N.
 * @since 3.3.2022
 *
 * ViewModel for the NoteFragment. Has functions for saving MPNotes,
 * determining MP status change and sorting the MPNotes
 */

class NoteFragmentViewModel (application: Application, private val mPID: Int): ViewModel(){

    //Reference to the Note -repository
    private val repository = NoteRepo(getNoteDatabase(application))

    //Reference to the MP -database and repository
    private val mPDB = getMPDatabase(application)
    private val mpRepo = MPRepo(getMPDatabase(application))

    //LiveData for the MP we are working with
    val currMP = Transformations.map(mpRepo.members) { it ->
        it.filter { it.personNum == mPID }[0]
    }

    //LiveData for the list of notes we want to display
    val notes = Transformations.map(repository.notes) { it ->
        it.filter { it.personNum == mPID }}

    //LiveData for navigating to NoteDetailsFragment
    private val _navigateToNoteDetails = MutableLiveData<Int?>()
        val navigateToNoteDetails : LiveData<Int?>
            get() = _navigateToNoteDetails

    // Reference to the MPNote-Database
    private val noteDB = getNoteDatabase(application)

    //Function for adding a note to the database
    suspend fun addNoteToDatabase(opinion: Boolean, note: String): Boolean {
        //Saving the current time to variables
        val localDateTime = LocalDateTime.now()
        val day = localDateTime.dayOfMonth
        val month = localDateTime.monthValue
        val year = localDateTime.year

        //Create an instance of an MPNote
        val newNote = MPNote(0, mPID, note, opinion, day, month, year)

        //Insert the new note to the Database
        return try {
            noteDB.noteDatabaseDao.insertNote(newNote)
            true
        } catch (e: Exception) {
            Timber.e(e)
            false
        }
    }

    //We evaluate whether the current status of an MP in the MP-Database should change
    suspend fun statusChangeCheck (currentMP: DatabaseMemberOfFinnishParliament) {

        //Get the current list of notes from the repo's LiveData
        val currentNoteList = notes.value

        //Check if the note data exists
        if (currentNoteList.isNullOrEmpty()) {
            //If doesn't, the status is neutral
            currentMP.status = 0
            mPDB.mPDao.update(currentMP)
        } else {
            //Grabbing only the notes which connect to this MP
            val notesOnThisMP = currentNoteList.filter {
                it.personNum == mPID}

            //Filtering the notes to positive and negative ones
            var positives = 0
            var negatives = 0
            //Check if we have notes
            if (notesOnThisMP.isNotEmpty()) {
                //Add 1 to positives/negatives for each note's opinion
                notesOnThisMP.map {if (it.opinion) positives++ else negatives++}
            }

            //Deciding the current overall opinion, neutral is 0
            val currentOverallOpinion: Int = when {
                //Mostly negative notes
                negatives > positives -> -1

                //Mostly positive notes
                negatives < positives -> 1
                else -> 0
            }

            //Check what the current status of the MP is and update if
            //it differs from currentOpinion
            if (currentMP.status != currentOverallOpinion) {
                //Update if needed
                currentMP.status = currentOverallOpinion
                mPDB.mPDao.update(currentMP)
            }
        }
    }

    fun sortNotesBy(sortNumber: Int) : List<MPNote>{
        //Get the current list of notes from the repo's LiveData
        val currentNoteList = notes.value

        val notesOnThisMP = if (!currentNoteList.isNullOrEmpty()) {
            //Grabbing only the notes which connect to this MP
            currentNoteList.filter { it.personNum == mPID }
        } else listOf()

        //Return the sorted MPNote-list to be displayed with the RecyclerView
        return when (sortNumber) {
            0 ->notesOnThisMP.sortedByDescending { it.id }
            1 ->notesOnThisMP.sortedByDescending { it.opinion }
            else -> {notesOnThisMP}
        }
    }

    //When a note is clicked
    fun onNoteCLicked(noteID: Int) {
        //Initiate the navigation
        _navigateToNoteDetails.value = noteID
    }
    //To be called when navigation is done
    fun navigationComplete() {
        //Deactivate the navigation init
        _navigateToNoteDetails.value = null
    }
}
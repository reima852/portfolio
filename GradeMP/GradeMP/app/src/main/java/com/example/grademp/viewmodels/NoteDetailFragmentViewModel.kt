package com.example.grademp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.grademp.repositories.NoteRepo
import com.example.grademp.room.getNoteDatabase
import kotlinx.coroutines.launch

/**
 * @author Reima N.
 * @since 5.3.2022
 *
 * ViewModel fot the NoteDetailFragment. Has functions for deleting and updating MPNotes.
 */

class NoteDetailFragmentViewModel (application: Application, private val noteID: Int): ViewModel(){

    //Reference to the repository
    private val repository = NoteRepo(getNoteDatabase(application))

    //LiveData for the current MPNote we want to display
    val currentNote = Transformations.map(repository.notes) { it ->
        it.filter { it.id == noteID }[0]}

    //LiveData for navigating back
    private val _navigateToNoteFragment = MutableLiveData<Boolean?>()
        val navigateToNoteFragment: LiveData<Boolean?>
            get () = _navigateToNoteFragment

    //Note deletion function
    private val noteDB = getNoteDatabase(application)
    fun deleteNote (application: Application) {

        val existenceCheckedNote = currentNote.value
        //Check if the current note exists
        existenceCheckedNote?.let {
            viewModelScope.launch {
                //Delete note
                noteDB.noteDatabaseDao.deleteNote(existenceCheckedNote)
                //Notify the user
                Toast.makeText(application, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Determines whether the note changed and updates changes to the MPNote-database
    fun updateIfChanged (application: Application, noteEditText: String) {

        val existenceCheckedNote = currentNote.value
        //Check if the current note exists
        existenceCheckedNote?.let {
            //Check if the texts differ
            if(noteEditText != existenceCheckedNote.note) {
                viewModelScope.launch {
                    //update note
                    existenceCheckedNote.note = noteEditText
                    noteDB.noteDatabaseDao.updateNote(existenceCheckedNote)
                    //Notify the user
                    Toast.makeText(application, "Note updated", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    //Navigation init function
    fun navigateBack() {
        _navigateToNoteFragment.value = true
    }

    //To be called when navigation is done
    fun navigationComplete () {
        _navigateToNoteFragment.value = null
    }
}
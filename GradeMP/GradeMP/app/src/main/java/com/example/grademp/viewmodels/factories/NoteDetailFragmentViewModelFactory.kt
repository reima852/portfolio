package com.example.grademp.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grademp.viewmodels.NoteDetailFragmentViewModel
import java.lang.IllegalArgumentException

/**
 * @author Reima N.
 * @since 5.3.2022
 *
 * Factory for NoteDetailFragment's ViewModel
 */

class NoteDetailFragmentViewModelFactory (private val application: Application, private val noteID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailFragmentViewModel::class.java)) {
            return  NoteDetailFragmentViewModel(application, noteID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
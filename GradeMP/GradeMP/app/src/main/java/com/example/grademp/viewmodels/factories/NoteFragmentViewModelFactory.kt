package com.example.grademp.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grademp.viewmodels.NoteFragmentViewModel
import java.lang.IllegalArgumentException

/**
 * @author Reima N.
 * @since 3.3.2022
 *
 * Factory for NoteFragment's ViewModel
 */

class NoteFragmentViewModelFactory (private val application: Application, private val mPID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteFragmentViewModel::class.java)) {
            return  NoteFragmentViewModel(application, mPID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.grademp.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grademp.viewmodels.DetailsFragmentViewModel
import java.lang.IllegalArgumentException

/**
 * @author Reima N.
 * @since 28.2.2022
 *
 * Factory for DetailsFragment's ViewModel
 */

class DetailsFragmentViewModelFactory (private val application: Application, private val mPID: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsFragmentViewModel::class.java)) {
            return  DetailsFragmentViewModel(application, mPID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
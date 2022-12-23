package com.example.grademp.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grademp.viewmodels.BrowsingFragmentViewModel
import java.lang.IllegalArgumentException


/**
 * @author Reima N. 
 * @since 25.2.2022
 *
 * Factory for BrowsingFragment's ViewModel
 */

class BrowsingFragmentViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrowsingFragmentViewModel::class.java)) {
            return  BrowsingFragmentViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
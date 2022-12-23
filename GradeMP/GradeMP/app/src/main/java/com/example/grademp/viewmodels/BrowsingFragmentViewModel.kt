package com.example.grademp.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.grademp.repositories.MPRepo
import com.example.grademp.room.DatabaseMemberOfFinnishParliament
import com.example.grademp.room.getMPDatabase
import kotlinx.coroutines.launch
import kotlin.streams.toList

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * ViewModel for the BrowsingFragment. Has functions for retrieving the MP -data, searching MP's and
 * sorting them in the RecyclerView.
 */

class BrowsingFragmentViewModel (application: Application) : AndroidViewModel(application) {

    //Reference to the repositories
    private val mPRepository = MPRepo(getMPDatabase(application))

    //The LiveData MP-list from the MP-repo
    private val mPListFromRepo = mPRepository.members

    //The default, full MP-list to be displayed with the RecyclerView.
    private var _currentMPList = mPListFromRepo
        val currentMPList: LiveData<List<DatabaseMemberOfFinnishParliament>>
            get() = _currentMPList

    //The LiveData For navigating to details-fragment
    private val _navigateToDetails = MutableLiveData<Int?>()
        val navigateToDetails: LiveData<Int?>
            get() = _navigateToDetails

    //If the database is empty, this function gets called
    fun retrieveMPs() {
        viewModelScope.launch {mPRepository.refreshMPs()}
    }

    //Search function for finding parties and MP's
    fun searchMPsWith(query: String, sortNumber: Int): List<DatabaseMemberOfFinnishParliament> {

        //The current version of the MP-list from the repo
        val snapshotFromMPListFromRepo = mPListFromRepo.value

        //The list which will contain all MP's that match the search
        var newList = listOf<DatabaseMemberOfFinnishParliament>()

        //First we make sure that the snapshot is not empty or null
        if (!snapshotFromMPListFromRepo.isNullOrEmpty()) {
            //The search result list for party
            newList = filterMPs(snapshotFromMPListFromRepo, query)
        }

        //Then decide whether we should return the search result list or the snapshot
        val returnList: List<DatabaseMemberOfFinnishParliament> =
            if (query.isBlank()) {
                //If no search parameters were provided we will return all MP's
                snapshotFromMPListFromRepo ?: listOf()
            } else {
                newList
            }

        //Return the new MP-list to be displayed with the RecyclerView
        return when (sortNumber) {
            0 ->returnList.sortedBy { it.first }
            1 ->returnList.sortedBy { it.last }
            2 ->returnList.sortedBy { it.party }
            3 ->returnList.sortedByDescending { it.status }
            else -> {returnList}
        }

    }

    //This function filters the search results based on input (not case sensitive)
    private fun filterMPs(memberList: List<DatabaseMemberOfFinnishParliament>, query: String):
            List<DatabaseMemberOfFinnishParliament>{

        val searchList = mutableListOf<DatabaseMemberOfFinnishParliament>()

        //First name filtering
        searchList += memberList.stream().filter{ it.first.contains(query, true) }.toList()

        //Last name filtering
        searchList += memberList.stream().filter{ it.last.contains(query, true) }.toList()

        //Party filtering
        searchList += memberList.stream().filter{ it.party.contains(query, true) }.toList()

        //Return the searchList without duplicates
        return searchList.toSet().toList()
    }

    //This function responds to the user clicking an MP and initiates navigation
    fun onMPClicked (id: Int) {
        _navigateToDetails.value = id
    }
    //To be called when navigation has been done
    fun navigationComplete() {
        _navigateToDetails.value = null
    }
}
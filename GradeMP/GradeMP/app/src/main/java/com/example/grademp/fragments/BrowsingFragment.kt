package com.example.grademp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.grademp.R
import com.example.grademp.databinding.FragmentBrowsingBinding
import com.example.grademp.viewmodels.BrowsingFragmentViewModel
import com.example.grademp.viewmodels.factories.BrowsingFragmentViewModelFactory
import com.example.grademp.viewmodels.recycleradapters.MPClickListener
import com.example.grademp.viewmodels.recycleradapters.MPListAdapter

/**
 * @author Reima N.
 * @since 25.2.2022
 *
 * This fragment offers different ways to search for MP's and sort the list in the RecyclerView.
 */
class BrowsingFragment : Fragment() {

    private lateinit var viewModel: BrowsingFragmentViewModel
    private lateinit var viewModelFactory: BrowsingFragmentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //DataBinding
        val binding: FragmentBrowsingBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_browsing, container, false)

        //ViewModel initialization
        val application = requireNotNull(this.activity).application
        viewModelFactory = BrowsingFragmentViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(BrowsingFragmentViewModel::class.java)

        //Adapter for the recycler view, which includes the click listener
        val adapter = MPListAdapter(MPClickListener { personNum ->
            viewModel.onMPClicked(personNum)
        })
        binding.mPListing.adapter = adapter

        //Observer for the MP -LiveData
        viewModel.currentMPList.observe(viewLifecycleOwner) {
            //Call for refresh if the db is empty
            if (it.isEmpty()) viewModel.retrieveMPs()

            //Every time the MP-list changes we need to resubmit it
            adapter.submitList(it)
        }

        //Observer for navigating to the DetailsFragment
        viewModel.navigateToDetails.observe(viewLifecycleOwner) { mPID ->
            mPID?.let {
                this.findNavController().navigate(
                    BrowsingFragmentDirections.actionBrowsingFragmentToDetailsFragment(it))

                viewModel.navigationComplete()
            }
        }

//------------------------CLickListeners for all buttons--------------------------------------------


        //Variable for storing the current sorting style of MP's in the RecyclerView
        var sortNumber = 0

        //CLickListener for radioGroup's buttons
        binding.apply {
            radioGroup.setOnCheckedChangeListener { _, checkedId -> when (checkedId) {
                    sortByFirstBtn.id -> sortNumber = 0
                    sortByLastBtn.id -> sortNumber = 1
                    sortByPartyBtn.id -> sortNumber = 2
                    sortByStatusBtn.id -> sortNumber = 3
                }
                //When a radio button is clicked we should immediately change
                //the RecyclerView's content to be sorted correctly.
                adapter.submitList(viewModel.searchMPsWith(
                    binding.editTextSearch.text.toString(), sortNumber))
            }
        }

        //ClickListener for search-button
        binding.searchBtn.setOnClickListener {
            //When the button is clicked we search from the existing MP-list and
            //change the RecyclerView's content to match the results.
            adapter.submitList(viewModel.searchMPsWith(
                binding.editTextSearch.text.toString(), sortNumber))
        }

        return binding.root

    }


}
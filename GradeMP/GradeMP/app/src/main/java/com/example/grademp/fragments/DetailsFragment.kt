package com.example.grademp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.grademp.R
import com.example.grademp.databinding.FragmentDetailsBinding
import com.example.grademp.viewmodels.DetailsFragmentViewModel
import com.example.grademp.viewmodels.factories.DetailsFragmentViewModelFactory

/**
 * @author Reima N.
 * @since 28.2.2022
 *
 * The fragment which displays information from a single MP in detail.
 * From here the user can move to the NoteFragment or go back to the browsingFragment.
 */

class DetailsFragment : Fragment() {

    private lateinit var viewModel: DetailsFragmentViewModel
    private lateinit var viewModelFactory: DetailsFragmentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //DataBinding
        val binding: FragmentDetailsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_details, container, false)

        //Get hold of the passed mPID-argument
        val arguments = DetailsFragmentArgs.fromBundle(requireArguments())

        //ViewModel initialization
        val application = requireNotNull(this.activity).application
        viewModelFactory = DetailsFragmentViewModelFactory(application, arguments.mpid)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(DetailsFragmentViewModel::class.java)

        //Setting the texts and image from the observed current MP
        viewModel.currentMP.observe(viewLifecycleOwner) {
            binding.apply {
                //Texts
                val name = "${it.first} ${it.last}"
                mpNameText.text = name

                val bornIn = "Born in ${it.bornYear}"
                mpBornYearText.text = bornIn

                val constituency = "Constituency: ${it.constituency}"
                mpConstituencyText.text = constituency

                val seatNum = "Seat in parliament: ${it.seatNumber}"
                mpSeatNumberText.text = seatNum

                val party = "Party: ${it.party}"
                mpPartyText.text = party

                val minister = if (it.minister) "Is a minister" else "Not a minister"
                mpMinisterText.text = minister

                //Set the link text to an empty string if it is not found
                val twitterLink = it.twitterLink.ifBlank { "" }
                mpTwitterLinkText.text = twitterLink

                //Image setter call
                viewModel.setImage(mpImageView, it.picture)
            }
        }

//------------------------CLickListeners for all buttons--------------------------------------------

        //Back-button
        binding.backButton.setOnClickListener{
            //Navigate back to the browsing fragment
            this.findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToBrowsingFragment())
        }
        //Note-button
        binding.addNoteButton.setOnClickListener {
            //Navigate to the note fragment, passing the current mpID
            this.findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToNoteFragment(arguments.mpid))
        }


        return binding.root
    }
}
package com.example.grademp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.grademp.R
import com.example.grademp.databinding.FragmentNoteBinding
import com.example.grademp.room.DatabaseMemberOfFinnishParliament
import com.example.grademp.viewmodels.NoteFragmentViewModel
import com.example.grademp.viewmodels.factories.NoteFragmentViewModelFactory
import com.example.grademp.viewmodels.recycleradapters.NoteClickListener
import com.example.grademp.viewmodels.recycleradapters.NoteListAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * @author Reima N.
 * @since 3.3.2022
 *
 * The fragment where the user can look at the existing notes of an MP and add more.
 */

class NoteFragment : Fragment() {

    private lateinit var viewModel: NoteFragmentViewModel
    private lateinit var viewModelFactory: NoteFragmentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //DataBinding
        val binding: FragmentNoteBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_note, container, false
        )

        //Get hold of the passed mPID-argument
        val arguments = DetailsFragmentArgs.fromBundle(requireArguments())

        //ViewModel initialization
        val application = requireNotNull(this.activity).application
        viewModelFactory = NoteFragmentViewModelFactory(application, arguments.mpid)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(NoteFragmentViewModel::class.java)

        //Adapter for the RecyclerView, which includes the click listener
        val adapter = NoteListAdapter(NoteClickListener { noteID ->
            viewModel.onNoteCLicked(noteID)
        })
        binding.noteListing.adapter = adapter

        //The variable which will contain the current MP-list
        var currentMP = DatabaseMemberOfFinnishParliament(
            0,"","",0,"",0,"",false,
        "",0,"")

        //Observer to keep the currMPList updated
        viewModel.currMP.observe(viewLifecycleOwner) {
            currentMP = it
            val newText = "Notes for ${it.first} ${it.last}"
            binding.noteHeaderText.text = newText
        }

        //The variable which has the current sorting option
        var currentSortNumber = 0

        //Observer for the ViewModel MPNote-list
        viewModel.notes.observe(viewLifecycleOwner) {
            //Submit the changed list of notes
            MainScope().launch {
                viewModel.statusChangeCheck(currentMP)
                adapter.submitList(viewModel.sortNotesBy(currentSortNumber))
            }
        }

        //Observer for navigating to NoteDetailsFragment
        viewModel.navigateToNoteDetails.observe(viewLifecycleOwner) { noteID ->
            noteID?.let {
                this.findNavController().navigate(
                    NoteFragmentDirections.actionNoteFragmentToNoteDetailFragment(
                        noteID, arguments.mpid)
                )
                viewModel.navigationComplete()
            }
        }

//------------------------CLickListeners for all buttons--------------------------------------------

        //Button actions for sorting options
        binding.radioGroupNote.setOnCheckedChangeListener { _, checkedId -> when (checkedId) {
            binding.sortByDateBtnNote.id -> currentSortNumber = 0
            binding.sortByTypeBtnNote.id -> currentSortNumber = 1 }
            //Submit the newly ordered list of notes
            MainScope().launch {
                adapter.submitList(viewModel.sortNotesBy(currentSortNumber))
            }
        }

        //The add-button
        binding.addNoteBtn.setOnClickListener {
            MainScope().launch {
                //Pass the state of the switch and the note to ViewModel's function
               val success = viewModel.addNoteToDatabase(
                   binding.opinionSwitch.isChecked, binding.noteEditText.text.toString())
                if (!success) {
                    //If the database update was unsuccessful, let the user know
                    Toast.makeText(
                        application.applicationContext,"Failed to add note", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        //To details -button
        binding.toDetailsBtn.setOnClickListener {
            //Navigate to DetailsFragment and pass the mPID
            this.findNavController().navigate(
                NoteFragmentDirections.actionNoteFragmentToDetailsFragment(arguments.mpid))
        }

        //MP-browser -button
        binding.mPBrowserBtn.setOnClickListener {
            //Navigate to BrowsingFragment
            this.findNavController().navigate(
                NoteFragmentDirections.actionNoteFragmentToBrowsingFragment())
        }

        return binding.root
    }
}
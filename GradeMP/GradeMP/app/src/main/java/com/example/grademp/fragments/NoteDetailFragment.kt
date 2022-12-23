package com.example.grademp.fragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.grademp.R
import com.example.grademp.databinding.FragmentNoteDetailBinding
import com.example.grademp.viewmodels.NoteDetailFragmentViewModel
import com.example.grademp.viewmodels.factories.NoteDetailFragmentViewModelFactory

/**
 * @author Reima N.
 * @since 5.3.2022
 *
 * This fragment shows the details of an MPNote.
 * The user can edit the note or delete it.
 */

class NoteDetailFragment : Fragment() {

    private lateinit var viewModel: NoteDetailFragmentViewModel
    private lateinit var viewModelFactory: NoteDetailFragmentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //DataBinding
        val binding: FragmentNoteDetailBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_note_detail, container, false)

        //Get hold of the passed mPID and noteID-arguments
        val arguments = NoteDetailFragmentArgs.fromBundle(requireArguments())

        //ViewModel initialization
        val application = requireNotNull(this.activity).application
        viewModelFactory = NoteDetailFragmentViewModelFactory(application, arguments.noteid)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(NoteDetailFragmentViewModel::class.java)

        //Observer for the current note
        viewModel.currentNote.observe(viewLifecycleOwner) { note ->
            //Set both the header and the editText attributes

            //Header text
            val opinionText = if (note.opinion) "Positive" else "Negative"
            val dateText = "${note.day}.${note.month}.${note.year}"
            val headerText = "$opinionText note from $dateText"
            binding.noteDetailHeader.text = headerText

            //editText text
            binding.noteDetailEditText.setText(note.note)

            //editText color and tint
            val color = Color.parseColor(if (note.opinion) "#9BD676" else "#E78989")
            binding.noteDetailEditText.setBackgroundColor(color)
            binding.noteDetailEditText.background.setTint(color)
        }

        //Navigation observer
        viewModel.navigateToNoteFragment.observe(viewLifecycleOwner) {
            it?.let {
                this.findNavController().navigate(
                    NoteDetailFragmentDirections.actionNoteDetailFragmentToNoteFragment(
                        arguments.mpid))

                viewModel.navigationComplete()
            }
        }

//------------------------CLickListeners for all buttons--------------------------------------------

        //Delete button initiates the note deletion and navigation out of the NoteDetailFragment
        binding.deleteBtn.setOnClickListener {
            Toast.makeText(application, "Hold to delete", Toast.LENGTH_SHORT).show()
        }
        binding.deleteBtn.setOnLongClickListener {
            viewModel.deleteNote(application)
            viewModel.navigateBack()
            true
        }

        //Return button initiates the navigation out of the NoteDetailFragment
        binding.returnBtn.setOnClickListener {
            viewModel.updateIfChanged(application, binding.noteDetailEditText.text.toString())
            viewModel.navigateBack()
        }

        return binding.root
    }
}
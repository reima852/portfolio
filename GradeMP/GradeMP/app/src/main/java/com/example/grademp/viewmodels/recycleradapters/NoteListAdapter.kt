package com.example.grademp.viewmodels.recycleradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grademp.databinding.NoteListItemBinding
import com.example.grademp.room.MPNote

/**
 * @author Reima N.
 * @since 3.3.2022
 *
 * This is the adapter for the RecyclerView that contains notes of MP's.
 * This file also contains a click listener class for the listed notes.
 */

class NoteListAdapter (private val clickListener: NoteClickListener):
    ListAdapter<MPNote, NoteListAdapter.NoteViewHolder>(DiffCallback){

    //Companion object for DiffUtil
    companion object DiffCallback : DiffUtil.ItemCallback<MPNote>() {

        override fun areItemsTheSame(oldItem: MPNote, newItem: MPNote): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MPNote, newItem: MPNote): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteListItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }


    //ViewHolder for the MP's
    class NoteViewHolder (private var binding: NoteListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        //Bind function, which binds the full date and opinion to the list item
        fun bind (note: MPNote, clickListener: NoteClickListener) {
            bindFullDateAndOpinion(binding.noteText, note)
            binding.clickListener = clickListener
            binding.note = note
        }
    }

}

//CLick listener for a recycler list item (MP)
class NoteClickListener(val clickListener: (noteID: Int) -> Unit) {

    fun onClick(mpNote: MPNote) = clickListener(mpNote.id)
}
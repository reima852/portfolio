package com.example.grademp.viewmodels.recycleradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grademp.databinding.MpListItemBinding
import com.example.grademp.room.DatabaseMemberOfFinnishParliament

/**
 * @author Reima N.)
 * @since 26.2.2022
 *
 * This is the adapter for the RecyclerView that contains MP's.
 * This file also contains a click listener class for the listed MP's.
 */

class MPListAdapter (private val clickListener: MPClickListener):
    ListAdapter<DatabaseMemberOfFinnishParliament, MPListAdapter.MPViewHolder>(DiffCallback){

    //Companion object for DiffUtil
    companion object DiffCallback : DiffUtil.ItemCallback<DatabaseMemberOfFinnishParliament>() {

        override fun areItemsTheSame(
            oldItem: DatabaseMemberOfFinnishParliament, newItem: DatabaseMemberOfFinnishParliament): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: DatabaseMemberOfFinnishParliament, newItem: DatabaseMemberOfFinnishParliament): Boolean {
            return oldItem.personNum == newItem.personNum
        }
    }

    //What happens when binding the list items to the RecyclerView
    override fun onBindViewHolder(holder: MPViewHolder, position: Int) {
        val mp = getItem(position)
        holder.bind(mp, clickListener)
    }

    //How the holder for each list item in the RecyclerView is created
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MPViewHolder {
        return MPViewHolder(MpListItemBinding.inflate(
            LayoutInflater.from(parent.context)))
    }


    //ViewHolder for the MP's
    class MPViewHolder (private var binding: MpListItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        //Bind function, which binds the full name and party of an MP to the RecyclerView list item
        fun bind (memberOfFinnishParliament: DatabaseMemberOfFinnishParliament,
                  clickListener: MPClickListener) {
            bindNamesAndParties(binding.mpListText, memberOfFinnishParliament)
            binding.clickListener = clickListener
            binding.mp = memberOfFinnishParliament
        }
    }

}

//CLick listener for a recycler list item (MP)
class MPClickListener(val clickListener: (mPID: Int) -> Unit) {

    fun onClick(memberOfFinnishParliament: DatabaseMemberOfFinnishParliament) =
        clickListener(memberOfFinnishParliament.personNum)
}
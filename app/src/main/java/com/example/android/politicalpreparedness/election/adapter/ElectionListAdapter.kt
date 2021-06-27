package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater.from
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionListItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener):
    ListAdapter<Election, ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ElectionViewHolder {
        return from(parent)
    }

    //Bind ViewHolder

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val anElection = getItem(position)

        //Setting up click listener
        holder.itemView.setOnClickListener{
            clickListener.onClick(anElection)
        }
        holder.bind(anElection)

    }



    // Add companion object to inflate ViewHolder (from)

    companion object {
        fun from(parent: ViewGroup):ElectionViewHolder {
            val layoutInflater = from(parent.context)
            val electionListItemBinding = ElectionListItemBinding.inflate(layoutInflater,parent,false)
            return ElectionViewHolder(electionListItemBinding)
        }
    }

}

// Create ElectionViewHolder
class ElectionViewHolder(var binding: ElectionListItemBinding):
    RecyclerView.ViewHolder(binding.root){
    fun bind(election: Election) {
        binding.election = election

        // Call binding.executePendingBindings(), which causes the update to execute immediately.
        binding.executePendingBindings()
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */

// Create ElectionDiffCallback
class ElectionDiffCallback: DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }
}

// Create ElectionListener
class ElectionListener(val clickListener: (electionId: Election) -> Unit) {
    fun onClick(election: Election) = clickListener(election)
}


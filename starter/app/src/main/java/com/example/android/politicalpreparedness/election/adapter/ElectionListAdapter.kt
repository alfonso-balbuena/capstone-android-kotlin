package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    class ElectionViewHolder private constructor(private val binding : ElectionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : Election, clickListener: ElectionListener) {
            binding.listener = clickListener
            binding.election = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup) : ElectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ElectionItemBinding.inflate(layoutInflater,parent,false)
                return ElectionViewHolder(binding)
            }
        }
    }


}

class ElectionListener(val listener: (election : Election) -> Unit) {
    fun onClick(election: Election) = listener(election)
}

class ElectionDiffCallback : DiffUtil.ItemCallback<Election> () {

    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
        return oldItem == newItem
    }

}
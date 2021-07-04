package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.RepresentativeItemBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter

import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter :
    ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}


class RepresentativeListener(val listener: (representative: Representative) -> Unit) {
    fun onClick(representative: Representative) = listener(representative)
}

class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {

    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

}

class RepresentativeViewHolder(val binding: RepresentativeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = RepresentativeItemBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }

    fun bind(item: Representative) {
        binding.representative = item
        binding.imgRepresentative.setImageResource(R.drawable.ic_profile)
        item.official.channels?.let { showSocialLinks(it) }
        if (item.official.urls == null || item.official.urls.isEmpty()) {
            disabled(binding.imgWeb)
        } else {
            item.official.urls?.let { showWWWLinks(it) }
        }
        binding.executePendingBindings()
    }


    private fun showSocialLinks(channels: List<Channel>) {
        Log.d("VIEWMODEL", "$channels")
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) {
            enableLink(binding.imgFb, facebookUrl)
        } else {
            disabled(binding.imgFb)
        }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) {
            enableLink(binding.imgTwitter, twitterUrl)
        } else {
            disabled(binding.imgTwitter)
        }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(binding.imgWeb, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
            .map { channel -> "https://www.facebook.com/${channel.id}" }
            .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
            .map { channel -> "https://www.twitter.com/${channel.id}" }
            .firstOrNull()
    }

    private fun disabled(view: ImageView) {
        view.visibility = View.GONE
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

}


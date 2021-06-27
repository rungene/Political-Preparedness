package com.example.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.RepresentativesItemBinding
import com.example.android.politicalpreparedness.network.models.Channel
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeViewHolder.Companion.from
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter:
    ListAdapter<Representative,
            RepresentativeViewHolder>(RepresentativeDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RepresentativeViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class RepresentativeViewHolder(private val representativesItemBinding:RepresentativesItemBinding): RecyclerView.ViewHolder(representativesItemBinding.root) {

    fun bind(item: Representative) {
        representativesItemBinding.representative = item
        representativesItemBinding.representativeImage.setImageResource(R.drawable.ic_profile)

        // Show social links ** Hint: Use provided helper methods
        item.official.channels?.let{showSocialLinks(it)}


        // Show www link ** Hint: Use provided helper methods
        item.official.urls?.let { showWWWLinks(it) }

        representativesItemBinding.executePendingBindings()
    }

    // Add companion object to inflate ViewHolder (from)

    companion object {
        fun from(parent: ViewGroup): RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val representativesItemBinding = RepresentativesItemBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(representativesItemBinding)
        }
    }


    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        if (!facebookUrl.isNullOrBlank()) { enableLink(representativesItemBinding.facebookIcon, facebookUrl) }

        val twitterUrl = getTwitterUrl(channels)
        if (!twitterUrl.isNullOrBlank()) { enableLink(representativesItemBinding.twitterIcon, twitterUrl) }
    }

    private fun showWWWLinks(urls: List<String>) {
        enableLink(representativesItemBinding.websiteIcon, urls.first())
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

//Create RepresentativeDiffCallback
class RepresentativeDiffCallback: DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return (oldItem.official == newItem.official && oldItem.office == newItem.office)
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }

}




// Create RepresentativeListener
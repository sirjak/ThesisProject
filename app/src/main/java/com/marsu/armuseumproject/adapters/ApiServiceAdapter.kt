package com.marsu.armuseumproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marsu.armuseumproject.R
import com.marsu.armuseumproject.database.Artwork
import com.marsu.armuseumproject.databinding.ArtListItemBinding
import com.squareup.picasso.Picasso

class ApiServiceAdapter: RecyclerView.Adapter<ApiServiceAdapter.ApiServiceViewHolder>() {

    private var artList: List<Artwork> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApiServiceViewHolder {
        val binding = ArtListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApiServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApiServiceViewHolder, position: Int) {
        holder.binding.art = artList[position]

        try {
            Picasso.get()
                .load(artList[position].primaryImageSmall)
                .fit()
                .centerCrop()
                .error(R.drawable.ic_not_found_vector)
                .into(holder.binding.artThumbnail)
            Log.d("onBindViewHolder", "image loaded succesfully")
        } catch (e: Exception) {
            Log.d("Exception when loading image", e.message.toString())
        }


    }

    override fun getItemCount() = artList.size

    /** Updates the data in the memberList variable. */
    fun setData(arts: List<Artwork>) {
        this.artList = arts
        notifyDataSetChanged()
        Log.d("data set at ApiServiceAdapter", artList.toString())
    }


    class ApiServiceViewHolder(val binding: ArtListItemBinding): RecyclerView.ViewHolder(binding.root)
}


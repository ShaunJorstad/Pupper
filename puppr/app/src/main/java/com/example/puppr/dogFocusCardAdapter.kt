package com.example.puppr

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.client_saved_dogs_base_card.view.*

class dogFocusCardAdapter(private val myDataset: Array<String>, private val userVM: UserViewModel)
    : RecyclerView.Adapter<dogFocusCardAdapter.MyViewHolder>() {

    class MyViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
    lateinit var parentViewGroup: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : dogFocusCardAdapter.MyViewHolder {

        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.client_focus_dogs_base_card, parent, false) as ImageView
        parentViewGroup = parent
        return dogFocusCardAdapter.MyViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: dogFocusCardAdapter.MyViewHolder, position: Int) {

        Log.d("YERT", myDataset[position])
        Glide.with(parentViewGroup)
            .load(myDataset[position])
            .into(holder.imageView)
    }

    override fun getItemCount() = myDataset.size
}
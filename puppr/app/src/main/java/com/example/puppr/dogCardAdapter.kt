package com.example.puppr

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.client_saved_dogs_base_card.view.*

/*
* myDataset will later be of type ints
* Each int will be a dogID tied to the user's profile's list of saved dogs
* onBindViewHolder will query the database for each dogID in myDataset
*   and add the info tied to the dogID in myDataset[position]
*/

class dogCardAdapter(private val myDataset: Array<String>, private val userVM: UserViewModel)
    : RecyclerView.Adapter<dogCardAdapter.MyViewHolder>() {

    class MyViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : dogCardAdapter.MyViewHolder {

        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.client_saved_dogs_base_card, parent, false) as CardView
        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val dogName: TextView = holder.cardView.base_card_dog_name
        val shelterName: TextView = holder.cardView.base_dog_shelter_name
        val docRef = userVM.database.collection("dogs").document(myDataset[position])
        docRef.get()
            .addOnSuccessListener { document ->
                dogName.text = document.data?.get("name").toString()
                val docRef2 = userVM.database.collection("shelter")
                    .document(document.data?.get("shelter").toString())
                docRef2.get()
                    .addOnSuccessListener { document2 ->
                        shelterName.text = document2.data?.get("name").toString()
                    }
            }

        dogName.text = myDataset[position]
    }

    override fun getItemCount() = myDataset.size
}
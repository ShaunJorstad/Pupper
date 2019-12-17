package com.example.puppr

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
    lateinit var parentViewGroup: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : dogCardAdapter.MyViewHolder {

        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.client_saved_dogs_base_card, parent, false) as CardView
        parentViewGroup = parent
        return MyViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        // Fields to be populated
        val dogName: TextView = holder.cardView.base_card_dog_name
        val shelterName: TextView = holder.cardView.base_dog_shelter_name
        val dogImage: ImageView = holder.cardView.base_card_dog_image

        // Get Dog info for dog id in myDataset[position]
        val docRef = userVM.database.collection("dogs").document(myDataset[position])
        docRef.get()
            .addOnSuccessListener { document ->
                dogName.text = document.data?.get("name").toString()

                if(userVM.userType == "shelter"){
                    shelterName.text = document.data?.get("bio").toString()
                }

                // Put image into dogImage ImageView
                Glide.with(parentViewGroup)
                    .load(document.data?.get("photos").toString()
                        .replace("[", "").replace("]", "").replace(" ", "").split(",").toTypedArray()[0])
                    .into(dogImage)

                if(userVM.userType == "user") {
                    val docRef2 = userVM.database.collection("shelter")
                        .document(document.data?.get("shelter").toString())
                    docRef2.get()
                        .addOnSuccessListener { document2 ->
                            shelterName.text = ""
                        }
                }

            }

        holder.cardView.setOnClickListener {
            userVM.savedDogsID = myDataset[position]

            // Adapter used for multiple fragments containing the same RecyclerView this determines where it's coming from
            if (userVM.userType != "user") {
                parentViewGroup.findNavController().navigate(R.id.action_shelterDogs_to_clientFocusDog)
            } else {

                parentViewGroup.findNavController().navigate(R.id.action_clientSavedDogs_to_clientFocusDog)
            }
        }

//        dogName.text = myDataset[position]
    }

    override fun getItemCount() = myDataset.size
}
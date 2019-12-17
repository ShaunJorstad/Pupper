package com.example.puppr


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.puppr.databinding.FragmentClientFocusDogBinding

/**
 * A simple [Fragment] subclass.
 */
class clientFocusDog : Fragment() {

    private lateinit var binding: FragmentClientFocusDogBinding
    private lateinit var userVM: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_focus_dog,
            container, false)

        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        build()

        return binding.root
    }

    private fun build() {

        if (userVM.savedDogsID != null) {

            // If coming in from the View Dog Side

            // Get info for selected dog
            val docRef = userVM.database.collection("dogs").document(userVM.savedDogsID ?: "This is not possible")
            docRef.get()
                .addOnSuccessListener { document ->
                    binding.focusDogName.text = document.data?.get("name").toString()
                    binding.focusDogBio.text = document.data?.get("bio").toString()
                    binding.focusDogBreed.text = document.data?.get("breed").toString()
                    binding.focusDogColor.text = document.data?.get("color").toString()
                    binding.focusDogHealth.text = document.data?.get("health").toString()
                    binding.focusDogAge.text = document.data?.get("age").toString()

                    val photoArray = document.data?.get("photos").toString()
                        .replace("[", "").replace("]", "").replace(" ", "").split(",").toTypedArray()

                    if (photoArray.isNotEmpty()) {
                        val helper = LinearSnapHelper()
                        viewManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                        viewAdapter = dogFocusCardAdapter(photoArray, userVM)
                        recyclerView = binding.focusDogCards.apply {
                            setHasFixedSize(true)
                            layoutManager = viewManager
                            adapter = viewAdapter
                        }
                        helper.attachToRecyclerView(recyclerView)
                    }
                }
        } else {

            // If coming in from the Saved Dog Side

            binding.focusDogName.text = userVM.dog.name

            val photoArray: Array<String> = userVM.dog.photo ?: arrayOf()

            // If photos exist put them in
            if (photoArray.isNotEmpty()) {

                val helper = LinearSnapHelper()
                viewManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
                viewAdapter = dogFocusCardAdapter(photoArray, userVM)
                recyclerView = binding.focusDogCards.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
                helper.attachToRecyclerView(recyclerView)
            }

            // Populate data
            binding.focusDogBio.text = userVM.dog.bio
            binding.focusDogBreed.text = userVM.dog.breed
            binding.focusDogColor.text = userVM.dog.color
            binding.focusDogHealth.text = userVM.dog.health
            binding.focusDogAge.text = userVM.dog.age
        }
    }
}

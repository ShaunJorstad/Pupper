package com.example.puppr


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentClientViewDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.client_view_dogs_base_card.view.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * A simple [Fragment] subclass.
 */
class clientViewDog : Fragment() {

    private lateinit var binding: FragmentClientViewDogBinding
    private lateinit var userVM: UserViewModel
    private lateinit var currentDogID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentClientViewDogBinding>(inflater, R.layout.fragment_client_view_dog,
            container, false)

        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val bottomNav: BottomNavigationView = binding.viewDogsBottomNav
        bottomNav.selectedItemId = bottomNav.menu[1].itemId
        bottomNav.menu[2].setOnMenuItemClickListener {

            this.findNavController().navigate(R.id.action_clientViewDog_to_clientSavedDogs)
            return@setOnMenuItemClickListener true
        }

        bottomNav.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_clientViewDog_to_clientPreferences)
            return@setOnMenuItemClickListener true
        }

        binding.likeButton.setOnClickListener {
            Log.i("YERT", "Liked")
        }

        binding.dislikeButton.setOnClickListener {
            generateNewCard()
        }

        binding.clientDogCard.setOnClickListener {
            this.findNavController().navigate(R.id.action_clientViewDog_to_clientFocusDog)
        }

        return binding.root
    }

    fun generateNewCard() {

        userVM.getDog()
        val docRef = userVM.database.collection("dogs").document(userVM.dogID)
        var dogName: String = ""
        var shelterName: String = ""
        var dogImage: Int = 0

        docRef.get()
            .addOnSuccessListener { document ->

                binding.dogName.text = document.data?.get("name").toString()
                binding.dogImage.setImageResource(R.mipmap.client_base_dog_foreground)

                val docRef2 = userVM.database.collection("shelters").document(document.data?.get("shelter").toString())
                docRef2.get()
                    .addOnSuccessListener { document2 ->
                        binding.shelterName.text = document2.data?.get("name").toString()
                    }
                    .addOnFailureListener { exception ->
                        Log.d("YERT", "get failed with ", exception)

                    }
            }
            .addOnFailureListener { exception ->
                Log.d("YERT", "get failed with ", exception)

            }
    }
}

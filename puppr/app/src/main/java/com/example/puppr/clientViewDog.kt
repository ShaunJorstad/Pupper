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
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentClientViewDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.client_view_dogs_base_card.view.*

/**
 * A simple [Fragment] subclass.
 */
class clientViewDog : Fragment() {

    private lateinit var binding: FragmentClientViewDogBinding
    private lateinit var dogModel: ViewDogModel
    private lateinit var card: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentClientViewDogBinding>(inflater, R.layout.fragment_client_view_dog,
            container, false)
        dogModel = ViewDogModel()
        card = binding.tempCard

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

        return binding.root
    }

    fun generateNewCard() {

        val cardInfo = LayoutInflater.from(this.context)
            .inflate(R.layout.client_view_dogs_base_card, binding.tempCard, false) as ConstraintLayout

        dogModel.getNewDog()
        cardInfo.dog_name.text = dogModel.dogName
        cardInfo.shelter_name.text = dogModel.shelterName
        cardInfo.dog_image.setImageResource(dogModel.dogImage)

        binding.tempCard.removeAllViews()
        binding.tempCard.addView(cardInfo)
    }
}

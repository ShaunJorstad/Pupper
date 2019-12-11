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
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

        MainScope().launch { loadDog(true) }

        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        Log.d("YERT", "ON CREATE")

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

            MainScope().launch { loadDog(true) }
        }

        binding.clientDogCard.setOnClickListener {
            this.findNavController().navigate(R.id.action_clientViewDog_to_clientFocusDog)
        }

        return binding.root
    }

    suspend fun loadDog(newDog: Boolean) {

        userVM.loadDog(newDog)
        binding.dogName.text = userVM.dog.name
        binding.dogImage.setImageResource(R.mipmap.client_base_dog_foreground)
        binding.shelterName.text = userVM.dog.shelter
        binding.dogName.refreshDrawableState()
        binding.dogImage.refreshDrawableState()
        binding.shelterName.refreshDrawableState()
    }
}

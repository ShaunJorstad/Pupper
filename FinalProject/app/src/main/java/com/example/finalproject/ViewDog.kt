package com.example.finalproject


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.finalproject.databinding.FragmentViewDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class ViewDog : Fragment() {

    private lateinit var binding: FragmentViewDogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentViewDogBinding>(inflater, R.layout.fragment_view_dog,
            container, false)

        val bottomNav: BottomNavigationView = binding.viewDogsBottomNav
        bottomNav.selectedItemId = bottomNav.menu[1].itemId
        bottomNav.menu[2].setOnMenuItemClickListener {

            this.findNavController().navigate(R.id.action_viewDog2_to_savedDogs)
            return@setOnMenuItemClickListener true
        }

        bottomNav.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_viewDog2_to_preference)
            return@setOnMenuItemClickListener true
        }

        binding.likeButton.setOnClickListener {
            Log.i("YERT", "Liked")
        }

        binding.dislikeButton.setOnClickListener {
            Log.i("YERT", "Dislike")
        }

        return binding.root
    }
}

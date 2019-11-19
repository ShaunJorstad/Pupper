package com.example.finalproject


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.finalproject.databinding.FragmentSavedDogsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class SavedDogs : Fragment() {

    private lateinit var binding: FragmentSavedDogsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentSavedDogsBinding>(inflater, R.layout.fragment_saved_dogs,
            container, false)

        val navBottom: BottomNavigationView = binding.savedDogsBottomNav
        navBottom.selectedItemId = navBottom.menu[2].itemId
        navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_savedDogs_to_viewDog2)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_savedDogs_to_preference)
            return@setOnMenuItemClickListener true
        }

        return binding.root
    }
}

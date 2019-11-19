package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentShelterDogsBinding
import com.example.puppr.databinding.FragmentShelterInformationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class ShelterDogs : Fragment() {
    private lateinit var binding: FragmentShelterDogsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentShelterDogsBinding>(inflater, R.layout.fragment_shelter_dogs,
            container, false)
        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.selectedItemId = navBottom.menu[0].itemId
        navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_shelterDogs_to_uploadDog)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[2].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_shelterDogs_to_shelterInformation)
            return@setOnMenuItemClickListener true
        }
        return binding.root
    }


}

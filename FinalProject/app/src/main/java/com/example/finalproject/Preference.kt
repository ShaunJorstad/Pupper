package com.example.finalproject


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.finalproject.databinding.FragmentPreferenceBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class Preference : Fragment() {

    private lateinit var binding: FragmentPreferenceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentPreferenceBinding>(inflater, R.layout.fragment_preference,
            container, false)

        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.selectedItemId = navBottom.menu[0].itemId
        navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_preference_to_viewDog2)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[2].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_preference_to_savedDogs)
            return@setOnMenuItemClickListener true
        }

        return binding.root
    }
}

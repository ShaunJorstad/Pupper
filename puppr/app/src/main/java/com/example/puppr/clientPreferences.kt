package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentClientPreferencesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class clientPreferences : Fragment() {

    private lateinit var binding: FragmentClientPreferencesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentClientPreferencesBinding>(inflater, R.layout.fragment_client_preferences,
            container, false)

        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.selectedItemId = navBottom.menu[0].itemId
        /*navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_clientPreferences_to_clientViewDog)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[2].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_clientPreferences_to_clientSavedDogs)
            return@setOnMenuItemClickListener true
        }*/

        return binding.root
    }
}

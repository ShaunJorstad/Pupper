package com.example.puppr


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentShelterSettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class ShelterSettings : Fragment() {

    private lateinit var binding: FragmentShelterSettingsBinding
    private lateinit var userVM: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentShelterSettingsBinding>(inflater, R.layout.fragment_shelter_settings,
            container, false)
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.signOutButton.setOnClickListener {view ->
            userVM.auth.signOut()
            view.findNavController().navigate(R.id.action_shelterDogs_to_userLoginFragment)
            Log.i("logOut", "logout button pressed")
        }
        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_shelterInformation_to_shelterDogs)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_shelterInformation_to_uploadDog)
            return@setOnMenuItemClickListener true
        }
        navBottom.selectedItemId = navBottom.menu[2].itemId
        return binding.root
    }

}

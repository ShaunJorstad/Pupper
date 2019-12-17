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
import com.example.puppr.databinding.FragmentClientPreferencesBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_client_preferences.view.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * A simple [Fragment] subclass.
 */
class clientPreferences : Fragment() {

    private lateinit var binding: FragmentClientPreferencesBinding
    private lateinit var userVM: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentClientPreferencesBinding>(inflater, R.layout.fragment_client_preferences,
            container, false)
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.signOutButton.setOnClickListener {view ->
            userVM.auth.signOut()
            view.findNavController().navigate(R.id.action_clientPreferences_to_userLoginFragment)
            Log.i("logOut", "logout button pressed")
        }

        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.selectedItemId = navBottom.menu[0].itemId
        navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_clientPreferences_to_clientViewDog)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[2].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_clientPreferences_to_clientSavedDogs)
            return@setOnMenuItemClickListener true
        }

        // Populate fields
        binding.prefName.text = "Name: "+userVM.user.name
        binding.prefAddress.text = "Address: "+userVM.user.address
        binding.prefBio.text = "User Bio: "+userVM.user.bio
        binding.prefEmail.text = "Email: "+userVM.user.email
        binding.prefPhone.text = "Phone Number: "+userVM.user.phone.toString()

        return binding.root
    }
}

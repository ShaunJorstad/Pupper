package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentUploadDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class UploadDog : Fragment() {
    private lateinit var binding: FragmentUploadDogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentUploadDogBinding>(inflater, R.layout.fragment_upload_dog,
            container, false)
        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_uploadDog_to_shelterDogs)
            return@setOnMenuItemClickListener true
        }
        navBottom.selectedItemId = navBottom.menu[1].itemId
        navBottom.menu[2].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_uploadDog_to_shelterInformation)
            return@setOnMenuItemClickListener true
        }

        return binding.root
    }


}

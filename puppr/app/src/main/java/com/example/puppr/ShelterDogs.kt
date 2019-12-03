package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puppr.databinding.FragmentShelterDogsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class ShelterDogs : Fragment() {
    private lateinit var binding: FragmentShelterDogsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentShelterDogsBinding>(inflater, R.layout.fragment_shelter_dogs,
            container, false)

        val myArray: Array<String> = arrayOf("Regenald", "Coregenald", "Trogenald", "Fogenald", "Figenald", "Sigenald", "Sevengenald", "Eigenald")

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = dogCardAdapter(myArray)
        recyclerView = binding.dogCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

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

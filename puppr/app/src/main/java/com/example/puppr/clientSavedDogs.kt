package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.puppr.databinding.FragmentClientSavedDogsBinding
import com.example.puppr.databinding.FragmentClientViewDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class clientSavedDogs : Fragment() {

    private lateinit var binding: FragmentClientSavedDogsBinding
    private lateinit var dogCard: CardView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentClientSavedDogsBinding>(inflater, R.layout.fragment_client_saved_dogs,
            container, false)

        val myArray: Array<String> = arrayOf("One", "Two", "Three", "Four", "Five")

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = dogCardAdapter(myArray)
        recyclerView = binding.dogCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        val navBottom: BottomNavigationView = binding.savedDogsBottomNav
        navBottom.selectedItemId = navBottom.menu[2].itemId
        navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_clientSavedDogs_to_clientViewDog)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_clientSavedDogs_to_clientPreferences)
            return@setOnMenuItemClickListener true
        }

        return binding.root
    }
}

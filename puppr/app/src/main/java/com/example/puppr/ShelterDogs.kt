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
    private lateinit var userVM: UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Set up databinding
        binding = DataBindingUtil.inflate<FragmentShelterDogsBinding>(inflater, R.layout.fragment_shelter_dogs,
            container, false)

        //Set up view model
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        //Array list to save the dog id keys
        var myArray: ArrayList<String> = arrayListOf()
        var shelterDogsUploaded = userVM.shelter.dogs.toString()    //List of shelter dog IDs from the view model

        Log.i("Harry", "Before Sub: " + shelterDogsUploaded)
        shelterDogsUploaded = shelterDogsUploaded.substring(1,shelterDogsUploaded.length - 1)   //Trim Brackets
        Log.i("Harry", "After Sub: " + shelterDogsUploaded)
        val array = shelterDogsUploaded.split(",").toTypedArray()//Split on the comma delimeter into an array


        for(word in array){
            myArray.add(word.trim())    //Populate arraylist
        }

        myArray.removeAt(0) //Remove first element which is always null

        //Pupulate the recycler view
        viewManager = LinearLayoutManager(this.context)
        viewAdapter = dogCardAdapter(myArray.toTypedArray(), userVM)
        recyclerView = binding.dogCards.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        //Navagation info
        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.selectedItemId = navBottom.menu[2].itemId
        navBottom.menu[1].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_shelterDogs_to_uploadDog)
            return@setOnMenuItemClickListener true
        }
        navBottom.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_shelterDogs_to_shelterInformation)
            return@setOnMenuItemClickListener true
        }
        return binding.root
    }
}
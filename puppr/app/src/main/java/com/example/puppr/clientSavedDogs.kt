package com.example.puppr


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
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
    private lateinit var userVM: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentClientSavedDogsBinding>(inflater, R.layout.fragment_client_saved_dogs,
            container, false)

        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val myArray: ArrayList<String> = arrayListOf()

        val docRef = userVM.database.collection("users").document(userVM.userID ?: "I want to die")
        docRef.get()
            .addOnSuccessListener { document ->

                var likedDogs = document.data?.get("likedDogs").toString()
                likedDogs = likedDogs.substring(1, likedDogs.length - 1)
                val array = likedDogs.split(",").toTypedArray()

                for (word in array) {

                    myArray.add(word.trim())
                }

                viewManager = LinearLayoutManager(this.context)
                viewAdapter = dogCardAdapter(myArray.toTypedArray(), userVM)
                recyclerView = binding.dogCards.apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
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

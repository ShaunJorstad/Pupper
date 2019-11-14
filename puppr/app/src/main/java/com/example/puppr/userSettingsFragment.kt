package com.example.puppr


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentUserSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class userSettingsFragment : Fragment() {
    private lateinit var binding: FragmentUserSettingsBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private val TAG = "userSettings"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentUserSettingsBinding>(
            inflater,
            R.layout.fragment_user_settings, container, false
        )

        binding.signOutButton.setOnClickListener{view: View ->
            FirebaseAuth.getInstance().signOut()
            view.findNavController().navigate(R.id.action_userSettingsFragment_to_userLoginFragment)
            Log.i("logOut", "logout button pressed")
        }

        binding.userImage.setOnClickListener{
//            instantiate ui to upload new image
        }
        database = FirebaseDatabase.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference

        binding.testButton.setOnClickListener{view: View ->
//            val ref = database.getReference("message")
//            Log.i("userSettings", "$ref")
            databaseRef.child("users").child("clcdmYjLwDTcNcQ0xKrX").child("address").setValue("The Wall")
        }

        return binding.root
    }


}

package com.example.puppr


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userVM: UserViewModel
    private val TAG = "userSettings"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(
            inflater,
            R.layout.fragment_settings, container, false
        )
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
//        TODO: populate VIEWMODEL information here
        userVM.populateFields()
        if (userVM.userType == "user") {
//            displayUserInformation()
            Log.d(TAG, "User is a user ")
        }
        if (userVM.userType == "shelter") {
//            displayShelterInformation()
            Log.d(TAG, "user is a shelter")
        }
        else {
            Log.d(TAG, "usertype undefined " + userVM.userType)
        }

        return binding.root
    }

    private fun populateData(): DocumentSnapshot? {
        var userDocument: DocumentSnapshot? = null
        var firestoreUser =
            userVM.database.collection("users").document(userVM.auth.currentUser!!.uid)
        firestoreUser.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userDocument = document
                }
            }
        return userDocument
    }

    private fun displayUserInformation() {
        var userImageRef =
            userVM.storage.reference.child("users/" + userVM.auth.currentUser!!.uid + "/userPhoto.jpg")
        val ONE_MEGABYTE: Long = 1024 * 1024 * 5
        userImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            var test = it!!
            var bmp = BitmapFactory.decodeByteArray(test, 0, test.size)
            binding.userImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 500, 500, false))
            // Data for "images/island.jpg" is returned, use this as needed
        }.addOnFailureListener {
            // Handle any errors
        }

//      assuming user
        var userDocument: DocumentSnapshot? = populateData()
        binding.userName.text = userDocument?.data!!["name"].toString()
        Log.i(TAG, "username: ${userDocument?.data}")
        userVM.database.collection("users").document(userVM.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                Log.i(TAG, "${document.data!!["address"]}")
                binding.userName.text = document.data!!["name"].toString()
            }
    }

    private fun displayShelterInformation() {

    }
}

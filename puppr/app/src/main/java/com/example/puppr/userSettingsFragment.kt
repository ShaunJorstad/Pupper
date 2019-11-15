package com.example.puppr


import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Document

/**
 * A simple [Fragment] subclass.
 */
class userSettingsFragment : Fragment() {
    private lateinit var binding: FragmentUserSettingsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
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

        binding.signOutButton.setOnClickListener { view: View ->
            FirebaseAuth.getInstance().signOut()
            view.findNavController().navigate(R.id.action_userSettingsFragment_to_userLoginFragment)
            Log.i("logOut", "logout button pressed")
        }

        binding.userImage.setOnClickListener {
            //            instantiate ui to upload new image
        }

        binding.testButton.setOnClickListener { view: View ->
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        var storageRef = storage.reference
        var userImageRef = storageRef.child("users/" + auth.currentUser!!.uid + "/userPhoto.jpg")
        val ONE_MEGABYTE: Long = 1024 * 1024 * 5
        userImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
            var test = it!!
            var bmp = BitmapFactory.decodeByteArray(test, 0, test.size)
            binding.userImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 500, 500, false))
            // Data for "images/island.jpg" is returned, use this as needed
        }.addOnFailureListener {
            // Handle any errors
        }

////      assuming user
//        var userDocument: DocumentSnapshot? = populateData()
//        binding.userName.text = userDocument?.data!!["name"].toString()
//        Log.i(TAG, "username: ${userDocument?.data}")
        db.collection("users").document(auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                Log.i(TAG, "${document.data!!["address"]}")
                binding.userName.text = document.data!!["name"].toString()
            }

        return binding.root
    }

    private fun populateData(): DocumentSnapshot? {
        var userDocument: DocumentSnapshot? = null
        var firestoreUser = db.collection("users").document(auth.currentUser!!.uid)
        firestoreUser.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userDocument = document
                }
            }
        return userDocument
    }
}

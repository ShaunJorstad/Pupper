package com.example.puppr

import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

data class User(
    var name: String? = "",
    var email: String? = "",
    var address: String? = "",
    var phone: Int? = -1,
    var agePrefHigh: Int? = -1,
    var agePrefLow: Int? = -1,
    var preferredBreeds: List<String>? = null,
    val likedDogs: List<String>? = null,
    val dislikedDogs: List<String>? = null
)

data class Shelter(
    var name: String? = "",
    var email: String? = "",
    var address: String? = "",
    var phone: Int? = -1,
    val dogs: List<String>? = null,
    val photos: List<String>? = null,
    val website: String? = null
)

class UserViewModel : ViewModel() {
    private val TAG = "UserViewModel"
    var userID: String? = ""
    var userType = ""
    var tempEmail = ""
    var tempPassword = ""
    lateinit var shelter: Shelter
    lateinit var user: User
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseFirestore
    lateinit var storage: FirebaseStorage
//    lateinit var userID: FirebaseUser

    init {
        Log.i(TAG, "View Model Created")
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        user = User()
        shelter = Shelter()
    }

    fun populateFields(){
//        load settings from firestore into user or shelter object
        // loads firestoreUser and sets userType
        var firestoreUser = database.collection("users").document(auth.currentUser!!.uid)
        firestoreUser.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    userType = "user"
                    Log.d(TAG, "This user type is!: $userType")
//                    TODO: pull all other user information from firebase into the viewmodel here
                } else {
                    Log.d(TAG, "No such document")
                    userType = "shelter"
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        if (userType == "shelter") {
            firestoreUser = database.collection("shelters").document(auth.currentUser!!.uid)
            firestoreUser.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

}
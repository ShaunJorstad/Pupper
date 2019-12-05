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
    var dogs: List<String>? = null,
    var photos: List<String>? = null,
    var website: String? = null
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
        Log.d(TAG, "inside populate fields")
        Log.d(TAG, "current userID: ${userID}")
//        load settings from firestore into user or shelter object
        // loads firestoreUser and sets userType
        var firestoreUser = database.collection("users").document(userID.toString())
        firestoreUser.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    userType = "user"
                    Log.d(TAG, "DocumentSnapshot: ${document}")
                    Log.d(TAG, "This user type is!: ${userType}")
                    //navigate to user fragment view thing
//                    TODO: pull all other user information from firebase into the viewmodel here
                } else {
                    database.collection("shelters")
                        .document(userID.toString()).get()
                        .addOnSuccessListener { innerDocument ->
                            if (innerDocument.data != null) {
                                Log.d(TAG, "DocumentSnapshot data: ${innerDocument.data}")
                                userType = "shelter"
                                Log.d(TAG, "This user type is!: ${userType}")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "get failed with ", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

}
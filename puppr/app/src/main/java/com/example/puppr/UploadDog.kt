package com.example.puppr
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentUploadDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.IOException
import kotlin.reflect.typeOf

/**
 * This class allows the animal shelter to upload a dog
 */

class UploadDog : Fragment() {
    private lateinit var binding: FragmentUploadDogBinding
    private lateinit var userVM: UserViewModel

    val TAG: String = "Urgent UploadDog"

    val packageManager: PackageManager? = context?.getPackageManager()
    val REQUEST_TAKE_PHOTO = 1;
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = DataBindingUtil.inflate<FragmentUploadDogBinding>(
            inflater, R.layout.fragment_upload_dog,
            container, false
        )

        // Bottom Nav Bar
        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.menu[2].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_uploadDog_to_shelterDogs)
            return@setOnMenuItemClickListener true
        }
        navBottom.selectedItemId = navBottom.menu[1].itemId
        navBottom.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_uploadDog_to_shelterInformation)
            return@setOnMenuItemClickListener true
        }

        // Fires when a shelter saves a dog
        binding.saveDogButton.setOnClickListener {
            // Get information about the dog from the XML file
            val dog = hashMapOf(
                "age" to binding.dogAge.getText().toString(),
                "bio" to binding.dogBio.getText().toString(),
                "breed" to binding.dogBreed.getText().toString(),
                "color" to binding.dogColor.getText().toString(),
                "dislikes" to null,
                "health" to null,
                "likes" to null,
                "name" to binding.dogName.getText().toString(),
                "photos" to null,
                "shelter" to userVM.userID
            )

            // Add the dog to the database
            userVM.database.collection("dogs").add(dog)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    Log.d(TAG, userVM.shelter.dogs.toString());

                    //Add the dog to shelter in the ViewModel
                    userVM.shelter.dogs = userVM.shelter.dogs?.plusElement(documentReference.id)
                    Log.d(TAG, userVM.shelter.dogs.toString());

                    //Add the dog to shelter records in the database
                    userVM.database.collection("shelters").document(userVM.userID.toString())
                        .update("dogs", userVM.shelter.dogs)

                        //if that works, clear the fields and make a toast indicating the dog was saved
                        .addOnSuccessListener {
                            binding.dogAge.setText("");
                            binding.dogBio.setText("");
                            binding.dogBreed.setText("");
                            binding.dogColor.setText("");
                            binding.dogName.setText("");
                            binding.healthHistory.setText("");
                            binding.vaccinations.setText("");
                            binding.currentHealth.setText("");
                            Toast.makeText(
                                getActivity(), "Dog saved!",
                                Toast.LENGTH_LONG
                            ).show();
                            Log.d(TAG, "Shelter Dogs successfully updated!")
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error updating Shelter Dogs", e) }
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        // Take a photo of the dog
        binding.captureDogButton.setOnClickListener {

        }

        return binding.root
    }
}

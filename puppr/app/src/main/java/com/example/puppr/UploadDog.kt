package com.example.puppr
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentUploadDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.reflect.typeOf

/**
 * A simple [Fragment] subclass.
 */




class UploadDog : Fragment() {
    private lateinit var binding: FragmentUploadDogBinding
    private lateinit var userVM: UserViewModel
    val REQUEST_IMAGE_CAPTURE = 1
    val TAG: String = "Urgent UploadDog"
    //val packageManager: PackageManager? = context?.getPackageManager()
    //val packageManager = android.content.pm.PackageManager.FEATURE_CAMERA
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = DataBindingUtil.inflate<FragmentUploadDogBinding>(inflater, R.layout.fragment_upload_dog,
            container, false)
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
        binding.saveDogButton.setOnClickListener {
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
            userVM.database.collection("dogs").add(dog)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                    Log.d(TAG, userVM.shelter.dogs.toString());
                    userVM.shelter.dogs = userVM.shelter.dogs?.plusElement(documentReference.id)
                    Log.d(TAG, userVM.shelter.dogs.toString());
                    userVM.database.collection("shelters").document(userVM.userID.toString())
                        .update("dogs", userVM.shelter.dogs)
                        .addOnSuccessListener {
                            binding.dogAge.setText("");
                            binding.dogBio.setText("");
                            binding.dogBreed.setText("");
                            binding.dogColor.setText("");
                            binding.dogName.setText("");
                            binding.healthHistory.setText("");
                            binding.vaccinations.setText("");
                            binding.currentHealth.setText("");
                            Toast.makeText(getActivity(), "Dog saved!",
                                Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Shelter Dogs successfully updated!")
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error updating Shelter Dogs", e) }
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            //binding.dogAge.
        }
//        binding.captureDogButton.setOnClickListener {
//            dispatchTakePictureIntent()
//
//        }

        return binding.root
    }
// TODO: this may need to be in a different activity
//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//
//            takePictureIntent.resolveActivity(context!!.getPackageManager())?.also {
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
//            }
//        }
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data.extras?.get("data") as Bitmap
//            binding.dogImage.setImageBitmap(imageBitmap)
//        }
//    }



}

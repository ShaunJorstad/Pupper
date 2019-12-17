package com.example.puppr

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.puppr.databinding.FragmentUploadDogBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FieldValue
import java.io.ByteArrayOutputStream


/**
 * This class allows the animal shelter to upload a dog to the database
 */

class UploadDog : Fragment() {
    private lateinit var binding: FragmentUploadDogBinding
    private lateinit var userVM: UserViewModel

    val TAG: String = "UploadDog"

    lateinit var packageManager: PackageManager
    val REQUEST_IMAGE_CAPTURE = 1;
    val GET_FROM_GALLERY = 1;

    lateinit var file: Uri;
    lateinit var imageBitmap: Bitmap;

    var imageExists = false;
    var takePhoto = false;
    var uploadPhoto = false;

    lateinit var currentPhotoPath: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        packageManager = context!!.getPackageManager()

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
                "photos" to arrayListOf<String>(),
                "shelter" to userVM.userID
            )

            // Add the dog to the database
            userVM.database.collection("dogs").add(dog)
                .addOnSuccessListener { documentReference ->
                    var dogID = documentReference.id
                    Log.d(TAG, "DocumentSnapshot written with ID: ${dogID}")
                    Log.d(TAG, userVM.shelter.dogs.toString());

                    // Add the dog image to the database
                    if (imageExists) {
                        val ref = userVM.storage.reference.child("dogs/${dogID}/photo.jpg")
                        val stream = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        val bitmapdata = stream.toByteArray()
                        var uploadTask = ref.putBytes(bitmapdata)
                        if (uploadPhoto) {
                            uploadTask = ref.putFile(file)
                        }
                        val urlTask = uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result

                                // Add imageRef to the dog's list of images
                                userVM.dog.photo = arrayOf(task.result.toString())

                                // Push the Dogs changed list of images to the database
                                val dogRef = userVM.database.collection("dogs").document(dogID)
                                dogRef.update("photos", FieldValue.arrayUnion(task.result.toString()))
                                    .addOnSuccessListener { Log.d(TAG, "Image successfully updated!")}
                                    .addOnFailureListener { e -> Log.w(TAG, "Error updating image", e) }
                            } else {
                                Log.d(TAG, "Image ipload failure");
                            }
                        }
                    }

                    // Add the dog to shelter in the ViewModel
                    if(userVM.shelter.dogs != null) {
                        userVM.shelter.dogs = userVM.shelter.dogs?.plusElement(documentReference.id);
                    } else {
                        userVM.shelter.dogs = listOf(documentReference.id);
                    }

                    // Add the dog to shelter records in the database
                    userVM.database.collection("shelters").document(userVM.userID.toString())
                        .update("dogs", userVM.shelter.dogs)
                        .addOnSuccessListener {
                            // If that works, clear the fields and make a toast indicating the dog was saved
                            binding.dogAge.setText("");
                            binding.dogBio.setText("");
                            binding.dogBreed.setText("");
                            binding.dogColor.setText("");
                            binding.dogName.setText("");
                            binding.dogImage.setImageResource(android.R.color.transparent);

                            // Give confirmation that the dog was uploaded
                            Toast.makeText(
                                getActivity(), "Dog saved!",
                                Toast.LENGTH_LONG
                            ).show();
                            Log.d(TAG, "Shelter Dogs successfully updated!")
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error updating Shelter Dogs", e) }
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding dog", e) }
        }

        // Upload a photo of the dog
        binding.chooseDogButton.setOnClickListener {
            uploadPhoto = true;
            takePhoto = false;
            val galleryIntent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, GET_FROM_GALLERY)
        }

        // Take a photo of the dog
        binding.captureDogButton.setOnClickListener {
            takePhoto = true;
            uploadPhoto = false;
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
        return binding.root
    }

    // Listen for when a photo of the dog was taken
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (uploadPhoto && requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK && data!=null) {
            // Obtain image URI
            imageExists = true;
            file = data?.data!!

            // Add the image to the ImageView
            val targetW: Int = binding.dogImage.width
            Glide.with(this)
                .load(data?.data)
                .placeholder(R.mipmap.client_base_dog)
                .apply(RequestOptions().override(targetW, targetW))
                .optionalCenterCrop()
                .into(binding.dogImage)

        } else if (takePhoto && requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Obtain image URI
            imageExists = true;

            imageBitmap = data?.extras!!.get("data") as Bitmap
            val targetW: Int = binding.dogImage.width


            val matrix = Matrix()

            matrix.postRotate(90F)

            val scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, targetW, targetW, true)

            val rotatedBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )

            // Add the image to the ImageView
            Glide.with(this)
                .load(rotatedBitmap)
                .placeholder(R.mipmap.client_base_dog)
                .apply(RequestOptions().override(targetW, targetW))
                .optionalCenterCrop()
                .into(binding.dogImage)



        } else {
            Toast.makeText(context, "Error loading image", Toast.LENGTH_LONG)
        }
    }
}

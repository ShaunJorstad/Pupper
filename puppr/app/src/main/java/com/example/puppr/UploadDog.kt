package com.example.puppr


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import android.content.pm.PackageManager
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentUploadDogBinding
import com.google.android.gms.common.wrappers.PackageManagerWrapper
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * A simple [Fragment] subclass.
 */
class UploadDog : Fragment() {
    private lateinit var binding: FragmentUploadDogBinding
    val REQUEST_IMAGE_CAPTURE = 1
    //val packageManager: PackageManager? = context?.getPackageManager()
    //val packageManager = android.content.pm.PackageManager.FEATURE_CAMERA
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentUploadDogBinding>(inflater, R.layout.fragment_upload_dog,
            container, false)
        val navBottom: BottomNavigationView = binding.preferenceBottomNav
        navBottom.menu[0].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_uploadDog_to_shelterDogs)
            return@setOnMenuItemClickListener true
        }
        navBottom.selectedItemId = navBottom.menu[1].itemId
        navBottom.menu[2].setOnMenuItemClickListener {
            this.findNavController().navigate(R.id.action_uploadDog_to_shelterInformation)
            return@setOnMenuItemClickListener true
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

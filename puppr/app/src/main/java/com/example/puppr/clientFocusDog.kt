package com.example.puppr


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.puppr.databinding.FragmentClientFocusDogBinding

/**
 * A simple [Fragment] subclass.
 */
class clientFocusDog : Fragment() {

    private lateinit var binding: FragmentClientFocusDogBinding
    private lateinit var userVM: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_focus_dog,
            container, false)

        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        build()

        return binding.root
    }

    private fun build() {

        if (userVM.savedDogsID != null) {

            val docRef = userVM.database.collection("dogs").document(userVM.savedDogsID ?: "This is not possible")
            docRef.get()
                .addOnSuccessListener { document ->
                    binding.focusDogName.text = document.data?.get("name").toString()
                    binding.focusDogImage.setImageResource(R.mipmap.client_base_dog_foreground)
                    binding.focusDogBio.text = document.data?.get("bio").toString()
                    binding.focusDogBreed.text = document.data?.get("breed").toString()
                    binding.focusDogColor.text = document.data?.get("color").toString()
                    binding.focusDogHealth.text = document.data?.get("health").toString()
                    binding.focusDogAge.text = document.data?.get("age").toString()
                }
        }

        binding.focusDogName.text = userVM.dog.name
        binding.focusDogImage.setImageResource(R.mipmap.client_base_dog_foreground)
        binding.focusDogBio.text = userVM.dog.bio
        binding.focusDogBreed.text = userVM.dog.breed
        binding.focusDogColor.text = userVM.dog.color
        binding.focusDogHealth.text = userVM.dog.health
        binding.focusDogAge.text = userVM.dog.age
    }
}

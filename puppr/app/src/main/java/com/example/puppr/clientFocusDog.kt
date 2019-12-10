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

        binding.focusDogName.text = userVM.dog.name
        binding.focusDogImage.setImageResource(R.mipmap.client_base_dog_foreground)
        binding.focusDogBio.text = userVM.dog.bio
        binding.focusDogBreed.text = userVM.dog.breed
        binding.focusDogColor.text = userVM.dog.color
        binding.focusDogHealth.text = userVM.dog.health
        binding.focusDogAge.text = userVM.dog.age
    }
}

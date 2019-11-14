package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentShelterSuoBinding

/**
 * A simple [Fragment] subclass.
 */
class ShelterSUOFragment : Fragment() {

    private lateinit var binding: FragmentShelterSuoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentShelterSuoBinding>(
            inflater,
            R.layout.fragment_shelter_suo, container, false
        )

        binding.emailSignInButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_shelterSUOFragment_to_shelterSignUpFragment)
        }

        return binding.root
    }

}
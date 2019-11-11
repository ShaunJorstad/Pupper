package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentSignupOptionsBinding

/**
 * A simple [Fragment] subclass.
 */
class SignupOptionsFragment : Fragment() {

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//                // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_signup_options, container, false)
//    }


    private lateinit var binding: FragmentSignupOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSignupOptionsBinding>(
            inflater,
            R.layout.fragment_signup_options, container, false
        )

        binding.emailSignInButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_signupOptionsFragment_to_userLoginFragment)
        }

        return binding.root
    }

}

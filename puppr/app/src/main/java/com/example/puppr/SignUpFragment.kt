package com.example.puppr


import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentSignUpBinding
import kotlinx.android.synthetic.main.fragment_sign_up.*

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var userVM: UserViewModel
    private var TAG = "userSignUp"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSignUpBinding>(
            inflater,
            R.layout.fragment_sign_up, container, false
        )
        // Inflate the layout for this fragment
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        binding.emailInput.text = SpannableStringBuilder(userVM.tempEmail)
        binding.passwordInput.text = SpannableStringBuilder(userVM.tempPassword)

        binding.signUpSubmitButton.setOnClickListener { joinWithEmailPassword() }
        binding.joinWithGoogleButton.setOnClickListener { joinWithGoogle() }
        binding.joinWithPhoneButton.setOnClickListener { joinWithPhone() }
        return binding.root
    }

    /**
     * creates an account in firebase
     */
    private fun joinWithEmailPassword() {
        var email = email_input.text.toString()
        var password = password_input.text.toString()
        userVM.tempEmail = email
        userVM.tempPassword = password

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                context,
                "Please enter text into email and password",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(
                context,
                "password must be 6 characters or longer",
                Toast.LENGTH_LONG
            )
            return
        }

        if (userVM.userType == "user") {
            view?.findNavController()?.navigate(R.id.action_signUpFragment_to_userPrefSetupFragment)
        } else if (userVM.userType == "shelter") {
            view?.findNavController()
                ?.navigate(R.id.action_signUpFragment_to_shelterPrefSetupFragment)

        } else {
            Toast.makeText(
                context,
                "UserType unspecified",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    /**
     * currently non-functioning button that would later authenticate a user with their google account
     */
    private fun joinWithGoogle() {
        Toast.makeText(context, "Google authentication is currently unavailable", Toast.LENGTH_LONG)
            .show()
    }

    /**
     * current non-functioning button that would later authenticate a user with their phone number
     */
    private fun joinWithPhone() {
        Toast.makeText(
            context,
            "Phone number authentication is currently unavailable",
            Toast.LENGTH_LONG
        )
            .show()
    }

}




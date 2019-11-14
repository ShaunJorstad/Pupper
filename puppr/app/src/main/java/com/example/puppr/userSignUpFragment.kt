package com.example.puppr

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentUserSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user_sign_up.*

/**
 * A simple [Fragment] subclass.
 */
class userSignUpFragment : Fragment() {
    private lateinit var binding: FragmentUserSignUpBinding
    private lateinit var auth: FirebaseAuth
    private var TAG = "SignUp"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentUserSignUpBinding>(
            inflater,
            R.layout.fragment_user_sign_up, container, false
        )
        auth = FirebaseAuth.getInstance()
        binding.signUpButton.setOnClickListener{
            registerUser(it)
        }

        return binding.root
    }

    private fun registerUser(view: View) {
        //            TODO: check input values for complexity constraints
        var email = email_input.text.toString()
        var password = password_input.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "please enter text into email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                else {
                    Log.d(TAG, "Successful account creation for user ${it.result?.user?.uid}")
                    view.findNavController().navigate(R.id.action_userSignUpFragment_to_userSettingsFragment)
                }
//                navigate to the user settings page

            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
            }
    }


}

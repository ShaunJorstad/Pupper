package com.example.puppr


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentUserLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user_login.*
import kotlinx.android.synthetic.main.fragment_user_login.email_input
import kotlinx.android.synthetic.main.fragment_user_login.password_input
import kotlinx.android.synthetic.main.fragment_user_sign_up.*

/**
 * A simple [Fragment] subclass.
 */
class userLoginFragment : Fragment() {
    private lateinit var binding: FragmentUserLoginBinding
    private lateinit var auth: FirebaseAuth
    private var TAG = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentUserLoginBinding>(
            inflater,
            R.layout.fragment_user_login, container, false
        )
        auth = FirebaseAuth.getInstance()
        binding.loginSubmitButton.setOnClickListener {
            loginUser(it)
        }
        binding.signUpButton.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_userLoginFragment_to_loginFragment)
        }
        return binding.root
    }

    private fun loginUser(view: View) {
        var email = email_input.text.toString()
        var password = password_input.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "please enter text into email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                else {
                    Log.d(TAG, "Successful signed in user ${it.result?.user?.uid}")
                    view.findNavController().navigate(R.id.action_userLoginFragment_to_userSettingsFragment2)
                }
//                navigate to the user settings page

            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(context, "invalid login credentials", Toast.LENGTH_SHORT).show()
            }
    }
}

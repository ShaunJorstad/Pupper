package com.example.puppr


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.puppr.databinding.FragmentUserLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_user_login.email_input
import kotlinx.android.synthetic.main.fragment_user_login.password_input

/**
 * A simple [Fragment] subclass.
 */
class userLoginFragment : Fragment() {
    private lateinit var binding: FragmentUserLoginBinding
    private lateinit var auth: FirebaseAuth
    private var TAG = "userLogIn"

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
            findNavController().navigate(R.id.action_userLoginFragment_to_clientSavedDogs)
            //loginUser(it)
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
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to sign in user: ${it.message}")
                view.hideKeyboard()
                Toast.makeText(context, "invalid login credentials", Toast.LENGTH_SHORT).show()
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            view?.findNavController()?.navigate(R.id.action_userLoginFragment_to_userSettingsFragment2)
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

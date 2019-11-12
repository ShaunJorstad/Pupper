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
        binding.loginSubmitButton.setOnClickListener{
            val email = email_input.text.toString()
            val password = password_input.text.toString()
        }
        binding.signUpButton.setOnClickListener{
            view?.findNavController()
                ?.navigate(R.id.action_userLoginFragment_to_loginFragment)
        }
        return binding.root
    }
}

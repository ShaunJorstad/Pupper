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
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.email_input
import kotlinx.android.synthetic.main.fragment_login.password_input

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var userVM: UserViewModel
    private var TAG = "userLogIn"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.signInSubmitButton.setOnClickListener {
            loginUser(it)
        }
        binding.userSignUpButton.setOnClickListener {
            userVM.userType = "user"
            userVM.tempEmail = email_input.text.toString()
            userVM.tempPassword = password_input.text.toString()
            it.findNavController()
                .navigate(R.id.action_userLoginFragment_to_signUpFragment)
        }
        binding.shelterSignUpButton.setOnClickListener {
            userVM.userType = "shelter"
            userVM.tempEmail = email_input.text.toString()
            userVM.tempPassword = password_input.text.toString()
            it.findNavController()
                .navigate(R.id.action_userLoginFragment_to_signUpFragment)
        }
        return binding.root
    }

    private fun loginUser(view: View) {
        var email = email_input.text.toString()
        var password = password_input.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "please enter text into email and password", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(context, "password must be longer than 5 characters", Toast.LENGTH_SHORT)
                .show()
            return
        }

        userVM.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                else {
                    if (userVM.userType == "user") {
                        Log.d(TAG, "Successful signed in user ${it.result?.user?.uid}")
                        view.findNavController()
                            .navigate(R.id.action_userLoginFragment_to_clientSavedDogs)
                    }
                    if (userVM.userType == "shelter") {
                        Log.d(TAG, "Successful signed in user ${it.result?.user?.uid}")
                        view.findNavController()
                            .navigate(R.id.action_userLoginFragment_to_shelterDogs)
                    }

                    // load user settings from firestore and populate in view module
//                    userVM.populateFields()
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
        val currentUser = userVM.auth.currentUser
        if (currentUser != null) {
            //set user type
            var firestoreUser =
                userVM.database.collection("users").document(userVM.auth.currentUser!!.uid)
            firestoreUser.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        userVM.userType = "user"
                        Log.d(TAG, "This user type is!: $userVM.userType")
                        //navigate to user fragment view thing
                        view?.findNavController()?.navigate(R.id.action_userLoginFragment_to_clientSavedDogs)
//                    TODO: pull all other user information from firebase into the viewmodel here
                    } else {
                        userVM.database.collection("shelters")
                            .document(userVM.auth.currentUser!!.uid).get()
                            .addOnSuccessListener { innerDocument ->
                                Log.d(TAG, "DocumentSnapshot data: ${innerDocument.data}")
                                userVM.userType = "shelter"
                                Log.d(TAG, "This user type is!: $userVM.userType")
                                view?.findNavController()?.navigate(R.id.action_userLoginFragment_to_shelterDogs)
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "get failed with ", exception)
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
            userVM.userID = currentUser.uid
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}